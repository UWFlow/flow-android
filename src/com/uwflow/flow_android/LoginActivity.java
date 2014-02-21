package com.uwflow.flow_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.uwflow.flow_android.network.FlowApiRequests;
import com.uwflow.flow_android.network.FlowAsyncClient;

public class LoginActivity extends Activity {
    private LoginButton loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.login_layout);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        if (FlowAsyncClient.getCookie() == null){
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
                @Override
                public void onUserInfoFetched(GraphUser user) {
                    if (user != null && FlowAsyncClient.getCookie() == null) {
                        FlowApiRequests.login(user.getId(), Session.getActiveSession().getAccessToken(), LoginActivity.this);
                        Intent myIntent = new Intent(LoginActivity.this, MainFlowActivity.class);
                        LoginActivity.this.startActivity(myIntent);
                    }
                }
            });
        } else {
            Intent myIntent = new Intent(this, MainFlowActivity.class);
            this.startActivity(myIntent);
        }
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    public void init(){
        FlowAsyncClient.init(this.getApplicationContext());
    }
}
