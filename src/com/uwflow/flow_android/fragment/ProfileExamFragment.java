package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfileExamAdapter;
import com.uwflow.flow_android.entities.Exam;
import com.uwflow.flow_android.entities.Friend;

import java.util.ArrayList;

public class ProfileExamFragment extends Fragment {

    protected ListView mExamsList;
    protected TextView mLastUpdatedText;
    protected Button mAddToCalBtn;
    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_exam_layout, container, false);
        mExamsList = (ListView)rootView.findViewById(R.id.exam_list);
        mLastUpdatedText = (TextView)rootView.findViewById(R.id.text_last_updated);
        mAddToCalBtn = (Button)rootView.findViewById(R.id.btn_exam_to_cal);

        // TODO: replace this arraylist with whatever real data source
        ArrayList<Exam> examList = new ArrayList<Exam>();
        for (int i = 0; i < 7; i++) {
            String name = "Phil 11" + i;
            String description = "Introduction to Philosophy: Knowledge and Reality";
            String date = "April 14(Mon) 7:30 - 10:00pm PAC 1,2,3";
            examList.add(new Exam(name, description, date));
        }

        mExamsList.setAdapter(new ProfileExamAdapter(examList, getActivity()));

        return rootView;
    }
}
