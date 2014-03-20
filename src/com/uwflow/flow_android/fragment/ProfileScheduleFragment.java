package com.uwflow.flow_android.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.activities.FullScreenImageActivity;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.ScheduleCourses;
import com.uwflow.flow_android.db_object.ScheduleImage;
import com.uwflow.flow_android.network.FlowDatabaseLoader;
import com.uwflow.flow_android.network.FlowImageLoader;
import com.uwflow.flow_android.network.FlowImageLoaderCallback;

public class ProfileScheduleFragment extends Fragment implements View.OnClickListener {
    private String mScheduleImageURL;
    private ImageView mImageSchedule;
    private Button mBtnShare;
    private View rootView;
    private ProfileScheduleReceiver profileScheduleReceiver;
    private TextView mEmptyScheduleView;
    private LinearLayout mScheduleContainer;
    protected FlowImageLoaderCallback scheduleImageCallback;
    protected FlowImageLoader flowImageLoader;
    protected FlowDatabaseLoader flowDatabaseLoader;
    protected ScheduleImage scheduleImage;

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
        mEmptyScheduleView = (TextView)rootView.findViewById(R.id.empty_profile_schedule);
        mScheduleContainer = (LinearLayout)rootView.findViewById(R.id.profile_schedule);

        mBtnShare.setEnabled(false);
        mBtnShare.setOnClickListener(this);

        // call this before setting up the receiver
        populateData();
        profileScheduleReceiver = new ProfileScheduleReceiver();
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(profileScheduleReceiver,
                new IntentFilter(Constants.BroadcastActionId.UPDATE_PROFILE_USER_SCHEDULE));
        return rootView;

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, mScheduleImageURL);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out my schedule!");
                startActivity(Intent.createChooser(shareIntent, "Share schedule"));
                break;
            case R.id.image_schedule:
                if (scheduleImage != null && scheduleImage.getImage() != null) {
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
        if (shouldShow) {
            mScheduleContainer.setVisibility(View.VISIBLE);
            mEmptyScheduleView.setVisibility(View.GONE);
        } else {
            mScheduleContainer.setVisibility(View.GONE);
            mEmptyScheduleView.setVisibility(View.VISIBLE);
        }
        mBtnShare.setEnabled(shouldShow);
    }

    protected void populateData() {
        final ProfileFragment profileFragment = ProfileFragment.convertFragment(getParentFragment());
        if (profileFragment == null)
            return;
        ScheduleCourses scheduleCourses = profileFragment.getUserSchedule();
        scheduleImage = flowDatabaseLoader.queryUserScheduleImage(profileFragment.getProfileID());
        if (scheduleImage != null) {
            mImageSchedule.setImageBitmap(scheduleImage.getImage());
            toggleShowSchedule(true);
        } else if (scheduleCourses != null && scheduleCourses.getScreenshotUrl() != null) {
            // assume the URL is valid and an image will be returned
            // TODO: change this conditional to 'if the image is successfully fetched'
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
        } else if (scheduleCourses == null) {
            // No schedule, load an empty state
            // TODO(david): Don't show this if the schedule is still loading from network
            toggleShowSchedule(false);
        }
    }

    protected class ProfileScheduleReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            populateData();
        }
    }
}


