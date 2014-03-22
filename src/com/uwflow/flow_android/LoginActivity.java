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
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends OrmLiteBaseActivity<FlowDatabaseHelper> {
    private static final String TAG = "LoginActivity";

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
                    if (FlowAsyncClient.getSessionCookie() == null ||
                            StringUtils.isEmpty(FlowAsyncClient.getCsrfToken())) {
                        // Clear existing cookies to force a login, in case we're missing a CSRF token
                        FlowAsyncClient.clearCookie();
                        FlowApiRequests.login(user.getId(), Session.getActiveSession().getAccessToken(),
                                new FlowApiRequestCallbackAdapter() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        loadDataAndLogin(response);
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
                }
            }
        });

        // TODO(david): Also ensure session cookie hasn't expired
        if (FlowAsyncClient.getSessionCookie() != null) {
            loadDataAndLogin(null);
        }
    }

    protected void loadDataAndLogin(JSONObject response) {
        // Save the CSRF token that we get back from the login response. This is needed for all non-GET requests.
        if (response != null) {
            try {
                String csrfToken = response.getString("csrf_token");
                FlowAsyncClient.setCsrfToken(csrfToken);
            } catch (JSONException e) {
                Log.e(TAG, "Could not extract CSRF token from JSON response " + response.toString());
            }
        }

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
