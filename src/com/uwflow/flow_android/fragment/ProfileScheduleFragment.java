package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.uwflow.flow_android.R;

public class ProfileScheduleFragment extends Fragment implements View.OnClickListener {

    private RadioGroup mRadioGroup;
    private ImageView mImageSchedule;
    private Button mBtnExportCal;
    private Button mBtnShare;
    private LinearLayout mScheduleListLayout;
    private LinearLayout mScheduleWeekLayout;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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


}


