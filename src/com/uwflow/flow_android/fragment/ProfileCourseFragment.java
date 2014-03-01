package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfileCoursesAdapter;
import com.uwflow.flow_android.adapters.ProfileFriendAdapter;
import com.uwflow.flow_android.entities.Course;
import com.uwflow.flow_android.entities.Friend;

import java.util.ArrayList;

public class ProfileCourseFragment extends Fragment{

    protected ListView mCoursesListView;
    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_course_layout, container, false);
        mCoursesListView = (ListView)rootView.findViewById(R.id.course_list);

        // TODO: replace this arraylist with whatever real data source
        ArrayList<Course> coursesList = new ArrayList<Course>();
        for (int i = 0; i < 12; i++) {
            String name = "Phil 11" + i;
            String description = "Introduction to Philosophy: Knowledge and Reality";
            coursesList.add(new Course(name, description));
        }

        mCoursesListView.setAdapter(new ProfileCoursesAdapter(coursesList, getActivity()));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }
}
