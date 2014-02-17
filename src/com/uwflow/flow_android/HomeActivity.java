package com.uwflow.flow_android;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.uwflow.flow_android.network.FlowApiRequestCallback;
import com.uwflow.flow_android.network.FlowApiRequests;
import org.json.JSONObject;

public class HomeActivity extends FlowActivity {
    protected TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        tv = (TextView) findViewById(R.id.text_field);
    }

    public void fetchProf(View v){
        FlowApiRequests.searchCourseProfessors(
                "psych101",
                new FlowApiRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        tv.setText(response.toString());
                    }
                });
    }

    public void fetchUser(View v){
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
