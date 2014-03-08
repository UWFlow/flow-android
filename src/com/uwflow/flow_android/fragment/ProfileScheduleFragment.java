package com.uwflow.flow_android.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.ScheduleCourses;

public class ProfileScheduleFragment extends Fragment implements View.OnClickListener {
    private String mProfileID;
    private String mScheduleImageURL;

    private RadioGroup mRadioGroup;
    private ImageView mImageSchedule;
    private Button mBtnExportCal;
    private Button mBtnShare;
    private LinearLayout mScheduleListLayout;
    private LinearLayout mScheduleWeekLayout;
    private Bitmap scheduleBitmap;
    private View rootView;
    private ProfileScheduleReceiver profileScheduleReceiver;
    protected Target scheduleImageCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mProfileID = getArguments() != null ? getArguments().getString(Constants.PROFILE_ID_KEY) : null;

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_schedule_layout, container, false);

        mRadioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group_view);
        mImageSchedule = (ImageView) rootView.findViewById(R.id.image_schedule);
        mImageSchedule.setOnClickListener(this);
        mBtnExportCal = (Button) rootView.findViewById(R.id.btn_export_calendar);
        mBtnShare = (Button) rootView.findViewById(R.id.btn_share);
        mScheduleListLayout = (LinearLayout) rootView.findViewById(R.id.list_layout);
        mScheduleWeekLayout = (LinearLayout) rootView.findViewById(R.id.week_layout);
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

        mBtnShare.setEnabled(false);
        mBtnShare.setOnClickListener(this);
        mBtnExportCal.setOnClickListener(this);
        // call this before setting up the receiver
        populateData();
        profileScheduleReceiver = new ProfileScheduleReceiver();
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(profileScheduleReceiver,
                new IntentFilter(Constants.BroadcastActionId.UPDATE_PROFILE_USER_SCHEDULE));
        return rootView;

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_export_calendar:
                // TODO: handle calendar export
                break;
            case R.id.btn_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, mScheduleImageURL);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out my schedule!");
                startActivity(Intent.createChooser(shareIntent, "Share schedule"));
                break;
            case R.id.image_schedule:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Fragment fullScreenImageFragment = new FullScreenImageFragment();

                //TODO:Replace this with original bitmap once the scheduleBitmap is being loaded properly from URL
                Bitmap catImage = BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),
                        R.drawable.kitty);
                Bundle bundle = new Bundle();
                bundle.putParcelable("ScheduleImage", scheduleBitmap);
                fullScreenImageFragment.setArguments(bundle);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, fullScreenImageFragment);
                transaction.addToBackStack(null);
                transaction.commit();
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


    protected void populateData() {
        final Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof ProfileFragment) {
            ScheduleCourses scheduleCourses = ((ProfileFragment) getParentFragment()).getUserSchedule();
            if (scheduleCourses != null && scheduleCourses.getScreenshotUrl() != null) {
                // assume the URL is valid and an image will be returned
                // TODO: change this conditional to 'if the image is successfully fetched'
                mScheduleImageURL = scheduleCourses.getScreenshotUrl();
                scheduleImageCallback = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                        scheduleBitmap = bitmap;
                        mImageSchedule.setImageBitmap(scheduleBitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable drawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable drawable) {

                    }
                };
                Picasso.with(getActivity().getApplicationContext())
                        .load(mScheduleImageURL).into(scheduleImageCallback);
                mBtnShare.setEnabled(true);
            }
        }
    }

    protected class ProfileScheduleReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            populateData();
        }
    }
}


