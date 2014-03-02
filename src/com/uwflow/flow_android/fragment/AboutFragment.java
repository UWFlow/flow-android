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
import com.uwflow.flow_android.network.FlowApiRequestCallback;
import com.uwflow.flow_android.network.FlowApiRequests;
import org.json.JSONObject;


public class AboutFragment extends Fragment {
    protected TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.about_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Button fetchProf = (Button) getView().findViewById(R.id.fetch_prof);
        Button fetchUser = (Button) getView().findViewById(R.id.fetch_user);
        Button fetchMe = (Button) getView().findViewById(R.id.fetch_me);
        tv= (TextView) getView().findViewById(R.id.text_field);
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

    public void fetchProf(){
        FlowApiRequests.searchCourseProfessors(
                "psych101",
                new FlowApiRequestCallback() {
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

    public void fetchUser(){
        FlowApiRequests.searchCourseUsers(
                "cs446",
                new FlowApiRequestCallback() {
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

    public void fetchMe(){
        FlowApiRequests.searchUser(new FlowApiRequestCallback() {
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
