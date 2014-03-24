package com.uwflow.flow_android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.analytics.tracking.android.EasyTracker;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.network.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends OrmLiteBaseActivity<FlowDatabaseHelper> {
    private static final String TAG = "LoginActivity";

    protected FlowDatabaseLoader databaseLoader;

    private LoginButton mLoginButton;
    private ProgressBar mLoginProgressBar;
    private Button mSkipLoginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        databaseLoader = new FlowDatabaseLoader(this.getApplicationContext(), getHelper());

        mLoginProgressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
        mLoginButton = (LoginButton) findViewById(R.id.login_button);
        mSkipLoginButton = (Button) findViewById(R.id.skip_login_button);

        // The only way to get to the login screen is if you're logged out.
        ((FlowApplication) getApplication()).setUserLoggedIn(false);

        mLoginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
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
                                        // Clear Facebook session
                                        if (Session.getActiveSession() != null) {
                                            Session.getActiveSession().closeAndClearTokenInformation();
                                        }
                                        Session.setActiveSession(null);

                                        // Remove any cookies
                                        FlowAsyncClient.clearCookie();

                                        if (error.toLowerCase().contains("create an account")) {
                                            // Prompt the user to create an account on uwflow.com or skip login
                                            promptCreateAccount();
                                        } else {
                                            // Toast with error text
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Oops! Couldn't log into Flow. " + error,
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                            Log.e(Constants.UW_FLOW, "Error logging in: " + error);
                                        }
                                    }
                                });
                    }
                }
            }
        });

        Button skipLoginButton = (Button) findViewById(R.id.skip_login_button);
        skipLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipLogin();
            }
        });

        // TODO(david): Also ensure session cookie hasn't expired
        if (FlowAsyncClient.getSessionCookie() != null) {
            loadDataAndLogin(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Google Analytics tracking
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Google Analytics tracking
        EasyTracker.getInstance(this).activityStop(this);
    }

    private void skipLogin() {
        ((FlowApplication)getApplication()).setUserLoggedIn(false);
        Intent myIntent = new Intent(LoginActivity.this, MainFlowActivity.class);
        LoginActivity.this.startActivity(myIntent);
    }

    private void promptCreateAccount() {
        new AlertDialog.Builder(LoginActivity.this)
                .setMessage("Oops, looks like you'll have to create an account on uwflow.com first.")
                .setPositiveButton("Go to uwflow.com", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://uwflow.com")));
                    }
                })
                .setNegativeButton("Skip login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        skipLogin();
                    }
                })
                .create()
                .show();
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

        // Indicate loading state and hide other actions
        mLoginProgressBar.setVisibility(View.VISIBLE);
        mLoginButton.setVisibility(View.GONE);
        mSkipLoginButton.setVisibility(View.GONE);

        databaseLoader.loadOrReloadProfileData(new ResultCollectorCallback() {
            @Override
            public void loadOrReloadCompleted() {
                ((FlowApplication)getApplication()).setUserLoggedIn(true);
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
