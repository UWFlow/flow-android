package com.uwflow.flow_android.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfileExamAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.Exam;
import com.uwflow.flow_android.db_object.Exams;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ProfileExamFragment extends Fragment {
    protected ListView mExamsList;
    protected TextView mLastUpdatedText;
    protected View rootView;
    protected ProfileExamAdapter profileExamAdapter;
    protected ProfileExamReceiver profileExamReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_exam_layout, container, false);
        mExamsList = (ListView) rootView.findViewById(R.id.exam_list);
        mExamsList.setEmptyView(rootView.findViewById(R.id.exam_list_empty));
        mLastUpdatedText = (TextView) rootView.findViewById(R.id.text_last_updated);
        populateData();
        profileExamReceiver = new ProfileExamReceiver();
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(profileExamReceiver,
                new IntentFilter(Constants.BroadcastActionId.UPDATE_PROFILE_USER_EXAMS));

        mExamsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: fire intent to add the exam to the users calendar
                Exam exam = (Exam) profileExamAdapter.getItem(position);
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).unregisterReceiver(profileExamReceiver);
        super.onDestroyView();
    }

    protected void populateData() {
        final Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof ProfileFragment) {
            Exams userExams = ((ProfileFragment) getParentFragment()).getUserExams();
            if (userExams != null) {
                // TODO(david): Ensure we get back last updated timestamp via database as well
                Timestamp lastUpdatedTimestamp = userExams.getLastUpdatedTimestamp();
                if (lastUpdatedTimestamp != null) {
                    String lastUpdatedString = new SimpleDateFormat("MMM d").format(lastUpdatedTimestamp);
                    mLastUpdatedText.setText("Last updated " + lastUpdatedString);
                }

                profileExamAdapter = new ProfileExamAdapter(userExams.getExams(), getActivity());
                mExamsList.setAdapter(profileExamAdapter);
            }
        }
    }

    protected class ProfileExamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            populateData();
        }
    }
}
