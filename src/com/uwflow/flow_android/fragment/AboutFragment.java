package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.*;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;
import org.json.JSONObject;

import java.util.ArrayList;


public class AboutFragment extends Fragment {
    protected TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.about_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button fetchProf = (Button) getView().findViewById(R.id.fetch_prof);
        Button fetchUser = (Button) getView().findViewById(R.id.fetch_user);
        Button fetchMe = (Button) getView().findViewById(R.id.fetch_me);
        tv = (TextView) getView().findViewById(R.id.text_field);
        fetchProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchProf();
            }
        });
        fetchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchUser();
            }
        });
        fetchMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchMe();
            }
        });
    }

    public void fetchProf() {
        FlowApiRequests.searchCourse(
                "psych101",
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getCourseCallback(CourseDetailedWrapper courseDetailedWrapper) {
                        if (courseDetailedWrapper != null)
                            tv.setText(tv.getText() + "Course Done\n");
                    }
                });
        FlowApiRequests.searchCourseUsers(
                "psych101",
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getCourseUsersCallback(CourseUserDetails courseUserDetails) {
                        if (courseUserDetails != null)
                            tv.setText(tv.getText() + "Course Users Done\n");
                    }
                });
        FlowApiRequests.searchCourseExams(
                "psych101",
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getCourseExamsCallback(ArrayList<Exam> exams) {
                        if (exams != null)
                            tv.setText(tv.getText() + "Course Exams Done\n");
                    }
                });
        FlowApiRequests.searchCourseProfessors(
                "psych101",
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getCourseProfessorCallback(ArrayList<Professor> professors) {
                        if (professors != null)
                            tv.setText(tv.getText() + "Course Professor Done\n");
                    }
                });
        FlowApiRequests.searchCourseSections(
                "psych101",
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getCourseSectionCallback(ArrayList<Section> sections) {
                        if (sections != null)
                            tv.setText(tv.getText() + "Course Section Done\n");
                    }
                });
    }

    public void fetchUser() {

        FlowApiRequests.searchCourseUsers(
                "cs446",
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        tv.setText(gson.toJson(response));
                    }

                    @Override
                    public void onFailure(String error) {

                    }
                });
    }

    public void fetchMe() {
        FlowApiRequests.searchUser(new FlowApiRequestCallbackAdapter() {
            @Override
            public void onSuccess(JSONObject response) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                tv.setText(gson.toJson(response));
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }
}
