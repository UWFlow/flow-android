package com.uwflow.flow_android.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.ScheduleCourse;
import com.uwflow.flow_android.db_object.ScheduleCourses;
import com.uwflow.flow_android.loaders.UserScheduleLoader;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;

import java.util.List;

public class ProfileScheduleFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ScheduleCourse>>, View.OnClickListener {
    private String mProfileID;

    private RadioGroup mRadioGroup;
    private ImageView mImageSchedule;
    private Button mBtnExportCal;
    private Button mBtnShare;
    private LinearLayout mScheduleListLayout;
    private LinearLayout mScheduleWeekLayout;
    private Bitmap scheduleBitmap;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	mProfileID = getArguments() != null ? getArguments().getString(Constants.PROFILE_ID_KEY) : null;

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_schedule_layout, container, false);

        mRadioGroup = (RadioGroup)rootView.findViewById(R.id.radio_group_view);
        mImageSchedule = (ImageView)rootView.findViewById(R.id.image_schedule);
        mBtnExportCal = (Button)rootView.findViewById(R.id.btn_export_calendar);
        mBtnShare = (Button)rootView.findViewById(R.id.btn_share);
        mScheduleListLayout = (LinearLayout)rootView.findViewById(R.id.list_layout);
        mScheduleWeekLayout = (LinearLayout)rootView.findViewById(R.id.week_layout);
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
        mBtnExportCal.setOnClickListener(this);

	if (mProfileID == null) {
	    getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_SCHEDULE_LOADER_ID, null, this);
	} else {
	    fetchScheduleImage(mProfileID);
	}

        return rootView;

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_export_calendar:
                // TODO: handle calendar export
                break;
            case R.id.btn_share:
                // TODO: handle profile share
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public Loader<List<ScheduleCourse>> onCreateLoader(int i, Bundle bundle) {
        return new UserScheduleLoader(getActivity(), ((MainFlowActivity)getActivity()).getHelper());

    }

    @Override
    public void onLoadFinished(Loader<List<ScheduleCourse>> arrayListLoader, List<ScheduleCourse> scheduleCourses) {
        if (!scheduleCourses.isEmpty() && scheduleCourses.get(0).getScheduleUrl() != null){
            Picasso.with(getActivity().getApplicationContext()).load(scheduleCourses.get(0).getScheduleUrl()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    scheduleBitmap = bitmap;
                    mImageSchedule.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {

                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ScheduleCourse>> arrayListLoader) {

    }

    private void fetchScheduleImage(String id){
	if (id == null) return;

	FlowApiRequests.getUserSchedule(
		id,
		new FlowApiRequestCallbackAdapter() {
		    @Override
		    public void getUserScheduleCallback(ScheduleCourses scheduleCourses) {
			if (scheduleCourses.getScreenshotUrl() != null) {
			    // assume the URL is valid and an image will be returned
			    // TODO: change this conditional to 'if the image is successfully fetched'
			    Picasso.with(getActivity().getApplicationContext())
				    .load(scheduleCourses.getScreenshotUrl()).into(mImageSchedule);
			}
		    }
		});
    }
}


