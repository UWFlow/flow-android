package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.uwflow.flow_android.R;

public class ProfileScheduleFragment extends Fragment implements View.OnClickListener {

    protected RadioGroup mRadioGroup;
    protected RadioButton mListViewRadioButton;
    protected RadioButton mWeekViewRadioButton;
    protected TextView mDateView;
    protected ImageView mImageSchedule;
    protected Button mBtnExportCal;
    protected Button mBtnShare;
    protected Button mBtnAddAlarm;

    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_schedule_layout, container, false);

        mRadioGroup = (RadioGroup)rootView.findViewById(R.id.radio_group_view);
        mListViewRadioButton = (RadioButton)rootView.findViewById(R.id.radio_list_view);
        mWeekViewRadioButton = (RadioButton)rootView.findViewById(R.id.radio_week_view);
        mDateView = (TextView)rootView.findViewById(R.id.text_view_date);
        mImageSchedule = (ImageView)rootView.findViewById(R.id.image_schedule);
        mBtnExportCal = (Button)rootView.findViewById(R.id.btn_export_calendar);
        mBtnShare = (Button)rootView.findViewById(R.id.btn_share);
        mBtnAddAlarm = (Button)rootView.findViewById(R.id.btn_schedule_profile);

        mImageSchedule.setImageResource(R.drawable.photo_profile_empty);

        mBtnShare.setOnClickListener(this);

        return rootView;

    }

    public void onClick(View v) {
        if (v == mBtnShare) {

        }
        if (v == mBtnExportCal) {

        }
        if (v == mBtnAddAlarm) {

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }


}


