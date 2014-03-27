package com.uwflow.flow_android.loaders;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.fragment.ProfileFragment;

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
            if (mBaseFragment != null) {
                final ProfileFragment profileFragment = (ProfileFragment) mBaseFragment;
                if (profileFragment != null && profileFragment.getProfileID() != null) {
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
