package com.uwflow.flow_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.uwflow.flow_android.network.ApiRequests;
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
                        ApiRequests.login(user.getId(), Session.getActiveSession().getAccessToken(), LoginActivity.this);
                    }
                }
            });
        } else {
            // Skip login
        }
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
