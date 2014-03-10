package com.uwflow.flow_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.network.*;
import org.json.JSONObject;

public class LoginActivity extends OrmLiteBaseActivity<FlowDatabaseHelper> {
    protected LoginButton loginButton;
    protected FlowDatabaseLoader databaseLoader;
    protected ProgressDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        databaseLoader = new FlowDatabaseLoader(this.getApplicationContext(), getHelper());
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null && FlowAsyncClient.getCookie() == null) {
                    FlowApiRequests.login(user.getId(), Session.getActiveSession().getAccessToken(),
                            new FlowApiRequestCallbackAdapter() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    loadDataAndLogin();
                                }

                                @Override
                                public void onFailure(String error) {
                                    loadDataAndLogin();
                                }
                            });
                }
            }
        });

        if (FlowAsyncClient.getCookie() != null) {
            loadDataAndLogin();
        }

        // TEMPORARY while the login isn't working:
        Button skipLoginButton = (Button) findViewById(R.id.temp_skip_login);
        skipLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LoginActivity.this, MainFlowActivity.class);
                LoginActivity.this.startActivity(myIntent);
            }
        });
    }

    protected void loadDataAndLogin() {
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle("Logging In");
        loadingDialog.setMessage("Loading ...");
        loadingDialog.show();

        databaseLoader.loadOrReloadProfileData(new ResultCollectorCallback() {
            @Override
            public void loadOrReloadCompleted() {
                loadingDialog.dismiss();
                Intent myIntent = new Intent(LoginActivity.this, MainFlowActivity.class);
                LoginActivity.this.startActivity(myIntent);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
}
