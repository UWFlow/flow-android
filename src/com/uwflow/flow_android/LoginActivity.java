package com.uwflow.flow_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.uwflow.flow_android.constant.Constants;
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
                if (user != null) {
                    if (FlowAsyncClient.getSessionCookie() == null) {
                        FlowApiRequests.login(user.getId(), Session.getActiveSession().getAccessToken(),
                                new FlowApiRequestCallbackAdapter() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        loadDataAndLogin();
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        // Toast with error text
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Oops! Couldn't sign into Flow.",
                                                Toast.LENGTH_LONG)
                                                .show();
                                        Log.e(Constants.UW_FLOW, "Error signing in: " + error);
                                    }
                                });
                    }
                } else {
                    /*
                     * User is not FB-authenticated. This may either mean
                     * 1. An unsuccessful sign-in (we may want to show a Toast)
                     * 2. User has just signed out (we don't want to show a Toast)
                     *
                     * This needs a better fix, but ATM we are putting a "finish" key
                     * in the intent bundle for LoginActivity in the case of 2.
                     *
                     * Below, we check for this key, and remove it after we've checked it once.
                     * We do this since ths callback (onUserInfoFetched) is called when
                     * we log out, but we don't want the "Error signing into FB" msg to
                     * be displayed. However, the user could try and fail at FB-authenticating
                     * during this session, so we remove it after we read it once.
                     */

                    if (getIntent().hasExtra("finish")) {
                        // Just logged out
                        getIntent().removeExtra("finish");
                    } else {
                        // Error FB-authenticating
                        // Toast with error text
                        Toast.makeText(
                                getApplicationContext(),
                                "Oops! Couldn't sign into Facebook.",
                                Toast.LENGTH_LONG)
                                .show();
                        Log.e(Constants.UW_FLOW, "Error signing into FB");
                    }
                }
            }
        });

        if (FlowAsyncClient.getSessionCookie() != null) {
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
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
}
