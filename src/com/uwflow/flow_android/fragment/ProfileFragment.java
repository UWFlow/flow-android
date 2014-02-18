package com.uwflow.flow_android.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.network.FlowApiRequestCallback;
import com.uwflow.flow_android.network.FlowApiRequests;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    protected TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Button fetchProf = (Button) getView().findViewById(R.id.fetch_prof);
        Button fetchUser = (Button) getView().findViewById(R.id.fetch_user);
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
    }

    public void fetchProf(){
        FlowApiRequests.searchCourseProfessors(
                "psych101",
                new FlowApiRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        tv.setText(response.toString());
                    }
                });
    }

    public void fetchUser(){
        FlowApiRequests.searchCourseUsers(
                "cs446",
                new FlowApiRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        tv.setText(response.toString());
                    }
                });
    }

}
