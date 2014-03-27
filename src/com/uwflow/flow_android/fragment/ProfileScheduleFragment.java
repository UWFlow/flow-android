package com.uwflow.flow_android.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.uwflow.flow_android.FlowApplication;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.activities.FullScreenImageActivity;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.ScheduleCourses;
import com.uwflow.flow_android.db_object.ScheduleImage;
import com.uwflow.flow_android.network.FlowDatabaseImageCallback;
import com.uwflow.flow_android.network.FlowDatabaseLoader;
import com.uwflow.flow_android.network.FlowImageLoader;
import com.uwflow.flow_android.network.FlowImageLoaderCallback;

public class ProfileScheduleFragment extends TrackedFragment implements View.OnClickListener {
    private static final String TAG = ProfileScheduleFragment.class.getSimpleName();

    private String mScheduleImageURL;
    private ImageView mImageSchedule;
    private Button mBtnShare;
    private View rootView;
    private ProfileScheduleReceiver profileScheduleReceiver;
    private TextView mEmptyScheduleView;
    private LinearLayout mScheduleContainer;
    private ProgressBar mLoadingProgress;
    protected FlowImageLoaderCallback scheduleImageCallback;
    protected FlowImageLoader flowImageLoader;
    protected FlowDatabaseLoader flowDatabaseLoader;
    protected ScheduleImage scheduleImage;
    protected ProfileRefreshReceiver updateReceiver;
    protected ScheduleCourses scheduleCourses;

    // variable used to track if the refresh button is pressed
    protected boolean forceReloadScheduleImage = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_schedule_layout, container, false);
        flowImageLoader = new FlowImageLoader(getActivity().getApplicationContext());
        flowDatabaseLoader = new FlowDatabaseLoader(getActivity().getApplicationContext(),
                ((MainFlowActivity) getActivity()).getHelper());
        scheduleImage = new ScheduleImage();
        mImageSchedule = (ImageView) rootView.findViewById(R.id.image_schedule);
        mImageSchedule.setOnClickListener(this);
        mBtnShare = (Button) rootView.findViewById(R.id.btn_share);
        mEmptyScheduleView = (TextView) rootView.findViewById(R.id.empty_profile_schedule);
        mScheduleContainer = (LinearLayout) rootView.findViewById(R.id.profile_schedule);
        mLoadingProgress = (ProgressBar) rootView.findViewById(R.id.profile_schedule_loading_progress);

        mBtnShare.setEnabled(false);
        mBtnShare.setOnClickListener(this);

        // call this before setting up the receiver
        populateData(/* isFromServer */ false);
        profileScheduleReceiver = new ProfileScheduleReceiver();
        updateReceiver = new ProfileRefreshReceiver();
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(profileScheduleReceiver,
                new IntentFilter(Constants.BroadcastActionId.UPDATE_PROFILE_USER_SCHEDULE));
        return rootView;

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                ((FlowApplication) getActivity().getApplication()).track("Share schedule intent");

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, mScheduleImageURL);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out my schedule!");
                startActivity(Intent.createChooser(shareIntent, "Share schedule"));
                break;
            case R.id.image_schedule:
                if (scheduleImage != null && scheduleImage.getImage() != null) {
                    ((FlowApplication) getActivity().getApplication()).track("Schedule image expanded");

                    Intent fullscreenImageIntent = new Intent(getActivity(), FullScreenImageActivity.class);
                    fullscreenImageIntent.putExtra("id", scheduleImage.getId());
                    getActivity().startActivity(fullscreenImageIntent);
                }
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).unregisterReceiver(profileScheduleReceiver);
        super.onDestroyView();
    }

    private void toggleShowSchedule(boolean shouldShow) {
        mImageSchedule.setVisibility(View.VISIBLE);
        mBtnShare.setEnabled(shouldShow);
        mLoadingProgress.setVisibility(View.GONE);

        if (shouldShow) {
            mScheduleContainer.setVisibility(View.VISIBLE);
            mEmptyScheduleView.setVisibility(View.GONE);
        } else {
            mScheduleContainer.setVisibility(View.GONE);
            mEmptyScheduleView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Render data to the views.
     * @param isFromServer Whether the data we received was just fetched from the server.
     */
    protected void populateData(final boolean isFromServer) {
        final ProfileFragment profileFragment = (ProfileFragment)getParentFragment();
        if (profileFragment == null)
            return;
        scheduleCourses = profileFragment.getUserSchedule();
        flowDatabaseLoader.queryUserScheduleImage(profileFragment.getProfileID(),
                new FlowDatabaseImageCallback() {
                    @Override
                    public void onScheduleImageLoaded(ScheduleImage image) {
                        scheduleImage = image;
                        if (scheduleImage != null && forceReloadScheduleImage == false) {
                            mImageSchedule.setImageBitmap(scheduleImage.getImage());
                            toggleShowSchedule(true);
                        } else if (scheduleCourses != null && scheduleCourses.getScreenshotUrl() != null) {
                            // assume the URL is valid and an image will be returned
                            // TODO: change this conditional to 'if the image is successfully fetched'
                            loadScheduleImage();
                        } else if (isFromServer) {
                            // Definitely no schedule, load an empty state
                            toggleShowSchedule(false);
                        }
                        forceReloadScheduleImage = false;
                    }
                });
    }

    protected void loadScheduleImage() {
        final ProfileFragment profileFragment = (ProfileFragment)getParentFragment();
        if (profileFragment == null)
            return;
        if (scheduleCourses != null && scheduleCourses.getScreenshotUrl() != null) {
            mScheduleImageURL = scheduleCourses.getScreenshotUrl();
            scheduleImageCallback = new FlowImageLoaderCallback() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    //add to database
                    scheduleImage = new ScheduleImage();
                    scheduleImage.setImage(bitmap);
                    scheduleImage.setId(profileFragment.getProfileID());
                    flowDatabaseLoader.updateOrCreateUserScheduleImage(scheduleImage);
                    toggleShowSchedule(true);
                }
            };
            flowImageLoader.loadImage(mScheduleImageURL, mImageSchedule, scheduleImageCallback);
        }
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(updateReceiver,
                new IntentFilter(Constants.BroadcastActionId.UPDATE_CURRENT_FRAGMENT));
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).unregisterReceiver(updateReceiver);
        super.onPause();

    }

    protected class ProfileScheduleReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            populateData(/* isFromServer */ true);
        }
    }

    protected class ProfileRefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // will reload new schedule image from network
            forceReloadScheduleImage = true;
        }
    }
}


