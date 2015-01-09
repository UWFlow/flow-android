package com.uwflow.flow_android.loaders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.uwflow.flow_android.FlowApplication;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.db_object.UserCourseDetail;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

public class UserLoader extends FlowAbstractDataLoader<User> {
    private LoaderUpdateReceiver userLoadedReceiver;
    public UserLoader(Context context, FlowDatabaseHelper flowDatabaseHelper, Fragment baseFragment) {
        super(context, flowDatabaseHelper, baseFragment);
    }

    protected void registerReceiver(){
        super.registerReceiver();
        // Start watching for changes in the app data.
        if (userLoadedReceiver == null) {
            userLoadedReceiver = new LoaderUpdateReceiver(this, Constants.BroadcastActionId.PROFILE_DATABASE_USER_LOADED);
        }
    }

    protected void unregisterReceiver(){
        super.unregisterReceiver();
        if (userLoadedReceiver != null) {
            LocalBroadcastManager.getInstance(this.getContext().getApplicationContext()).unregisterReceiver(userLoadedReceiver);
            userLoadedReceiver = null;
        }
    }

    @Override
    protected User loadDelegate() {
        try {
            if (mBaseFragment != null && mBaseFragment instanceof ProfileFragment) {
                final ProfileFragment profileFragment = (ProfileFragment) mBaseFragment;
                if (profileFragment != null && profileFragment.getProfileID() != null) {
                    // Make an API call just to check if the logged-in user is authorized to make this request
                    mBaseFragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FlowApiRequests.getUser(
                                    profileFragment.getProfileID(),
                                    new FlowApiRequestCallbackAdapter() {
                                        @Override
                                        public void onFailure(String error) {
                                            /*
                                             * ATM we don't have a way to see if there was a 403 Forbidden response, which indicates
                                             * that the logged-in user cannot access the profile being requested (not a FB friend).
                                             * We're assuming that is the case here in onFailure.
                                             * TODO: confirm the error before creating this explanation dialog.
                                             */
                                            AlertDialog alertDialog = new AlertDialog.Builder(profileFragment.getActivity())
                                                    .setTitle("Uh-Oh!")
                                                    .setMessage("You need to be a Facebook friend of this user to view their profile.")
                                                    .setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // Pop fragment.
                                                            profileFragment.getFragmentManager().popBackStackImmediate();
                                                        }
                                                    })
                                                    .setCancelable(false)
                                                    .show();

                                            // Log unauthorized profile access event.
                                            JSONObject properties = new JSONObject();
                                            try {
                                                properties.put("requestedProfileID", profileFragment.getProfileID());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            ((FlowApplication) profileFragment.getActivity().getApplication())
                                                    .track("Unauthorized profile access", properties);
                                        }
                                    }
                            );
                        }
                    });

                    Dao<User, String> userDao = flowDatabaseHelper.getUserDao();
                    QueryBuilder<User, String> queryBuilder =
                            userDao.queryBuilder();
                    List<User> userFriends = userDao.query(queryBuilder.where().eq("id", profileFragment.getProfileID()).prepare());
                    if (!userFriends.isEmpty()) {
                        return userFriends.get(0);
                    }
                } else {
                    Dao<User, String> userDao = flowDatabaseHelper.getUserDao();
                    QueryBuilder<User, String> queryBuilder =
                            userDao.queryBuilder();
                    List<User> userFriends = userDao.query(queryBuilder.where().eq(User.IS_ME, true).prepare());
                    if (!userFriends.isEmpty()) {
                        return userFriends.get(0);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return new User();
    }
}
