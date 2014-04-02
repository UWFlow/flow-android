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
import com.uwflow.flow_android.adapters.ProfileScheduleAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.ScheduleCourses;
import com.uwflow.flow_android.db_object.ScheduleImage;
import com.uwflow.flow_android.network.FlowDatabaseImageCallback;
import com.uwflow.flow_android.network.FlowDatabaseLoader;
import com.uwflow.flow_android.network.FlowImageLoader;
import com.uwflow.flow_android.network.FlowImageLoaderCallback;
import com.uwflow.flow_android.util.CourseUtil;

public class ProfileScheduleFragment extends TrackedFragment implements View.OnClickListener {
    private static final String TAG = ProfileScheduleFragment.class.getSimpleName();

    private RadioGroup mRadioGroup;
    private ImageView mImageSchedule;
    private Button mBtnShare;
    private FrameLayout mScheduleContainer;
    private LinearLayout mScheduleListLayout;
    private LinearLayout mScheduleWeekLayout;
    private ExpandableListView mScheduleListView;
    private View rootView;
    private TextView mEmptyWeekScheduleView;
    private ProgressBar mImageLoadingProgress;
    private ProgressBar mListLoadingProgress;

    private ProfileScheduleReceiver mProfileScheduleReceiver;
    private ProfileScheduleAdapter mScheduleListAdapter;
    private String mScheduleImageURL;
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
        mRadioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group_view);
        mImageSchedule = (ImageView) rootView.findViewById(R.id.image_schedule);
        mImageLoadingProgress = (ProgressBar) rootView.findViewById(R.id.schedule_img_loading_progress);
        mListLoadingProgress = (ProgressBar) rootView.findViewById(R.id.schedule_list_loading_progress);
        mBtnShare = (Button) rootView.findViewById(R.id.btn_share);
        mScheduleContainer = (FrameLayout) rootView.findViewById(R.id.schedule_container);
        mScheduleListLayout = (LinearLayout) rootView.findViewById(R.id.list_layout);
        mScheduleListView = (ExpandableListView) rootView.findViewById(R.id.schedule_list);
        mScheduleWeekLayout = (LinearLayout) rootView.findViewById(R.id.week_layout);
        mEmptyWeekScheduleView = (TextView) rootView.findViewById(R.id.empty_profile_schedule);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_list_view:
                        // List layout selected
                        mScheduleListLayout.setVisibility(View.VISIBLE);
                        mScheduleWeekLayout.setVisibility(View.GONE);
                        break;
                    case R.id.radio_week_view:
                        // Week layout selected
                        mScheduleListLayout.setVisibility(View.GONE);
                        mScheduleWeekLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        mBtnShare.setOnClickListener(this);
        mImageSchedule.setOnClickListener(this);

        // call this before setting up the receiver
        mProfileScheduleReceiver = new ProfileScheduleReceiver();
        updateReceiver = new ProfileRefreshReceiver();
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(mProfileScheduleReceiver,
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
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).unregisterReceiver(mProfileScheduleReceiver);
        super.onDestroyView();
    }

    private void showEmptyView(boolean show) {
        if (show) {
            mRadioGroup.setVisibility(View.GONE);
            mScheduleContainer.setVisibility(View.GONE);
            mEmptyWeekScheduleView.setVisibility(View.VISIBLE);
        } else {
            mRadioGroup.setVisibility(View.VISIBLE);
            mScheduleContainer.setVisibility(View.VISIBLE);
            mEmptyWeekScheduleView.setVisibility(View.GONE);
        }
    }

    private void showScheduleImage(boolean shouldShow) {
        mImageLoadingProgress.setVisibility(View.GONE);

        if (shouldShow) {
            mImageSchedule.setVisibility(View.VISIBLE);
            mBtnShare.setVisibility(View.VISIBLE);
        } else {
            mImageSchedule.setVisibility(View.GONE);
            mBtnShare.setVisibility(View.GONE);
        }
    }

    private void showScheduleList(boolean shouldShow) {
        mListLoadingProgress.setVisibility(View.GONE);

        if (shouldShow) {
            mScheduleListView.setVisibility(View.VISIBLE);
        } else {
            mScheduleListView.setVisibility(View.GONE);
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

        if (scheduleCourses != null && !scheduleCourses.getScheduleCourses().isEmpty()) {
            mScheduleListAdapter = new ProfileScheduleAdapter(scheduleCourses, getActivity());
            mScheduleListView.setAdapter(mScheduleListAdapter);
            mScheduleListAdapter.notifyDataSetChanged();
            CourseUtil.expandAllGroups(mScheduleListView);

            if (mScheduleListAdapter.getCount() == 0) {
                showEmptyView(true);
            } else {
                showEmptyView(false);
                showScheduleList(true);

                // Load schedule image
                flowDatabaseLoader.queryUserScheduleImage(profileFragment.getProfileID(),
                        new FlowDatabaseImageCallback() {
                            @Override
                            public void onScheduleImageLoaded(ScheduleImage image) {
                                scheduleImage = image;
                                if (scheduleImage != null && forceReloadScheduleImage == false) {
                                    mImageSchedule.setImageBitmap(scheduleImage.getImage());
                                    showScheduleImage(true);
                                } else if (scheduleCourses != null && scheduleCourses.getScreenshotUrl() != null) {
                                    // assume the URL is valid and an image will be returned
                                    // TODO: change this conditional to 'if the image is successfully fetched'
                                    loadScheduleImage();
                                } else if (isFromServer) {
                                    // Definitely no schedule, load an empty state
                                    showScheduleImage(false);
                                }
                                forceReloadScheduleImage = false;
                            }
                        });
            }
        } else if (isFromServer) {
            showEmptyView(true);
        }
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
                    showScheduleImage(true);
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


