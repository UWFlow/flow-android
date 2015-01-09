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
import com.uwflow.flow_android.db_object.UserFriends;
import com.uwflow.flow_android.fragment.ProfileFragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserFriendsLoader extends FlowAbstractDataLoader<UserFriends> {
    private LoaderUpdateReceiver userFriendsLoadedReceiver;
    public UserFriendsLoader(Context context, FlowDatabaseHelper flowDatabaseHelper, Fragment baseFragment) {
        super(context, flowDatabaseHelper, baseFragment);
    }

    protected void registerReceiver(){
        super.registerReceiver();
        // Start watching for changes in the app data.
        if (userFriendsLoadedReceiver == null) {
            userFriendsLoadedReceiver = new LoaderUpdateReceiver(this, Constants.BroadcastActionId.PROFILE_DATABASE_USER_FRIEND_LOADED);
        }
    }

    protected void unregisterReceiver(){
        super.unregisterReceiver();
        if (userFriendsLoadedReceiver != null) {
            LocalBroadcastManager.getInstance(this.getContext().getApplicationContext()).unregisterReceiver(userFriendsLoadedReceiver);
            userFriendsLoadedReceiver = null;
        }
    }

    @Override
    protected UserFriends loadDelegate() {
        // we first check if we should load from database or from the network
        if (mBaseFragment != null) {
            final ProfileFragment profileFragment = (ProfileFragment) mBaseFragment;
            if (profileFragment != null && profileFragment.getProfileID() != null) {
                // TODO add this call when we decide to add friends to user friend profile
                //return null;
            }
        }

        List<User> userFriends = new ArrayList<User>();
        try {
            Dao<User, String> userDao = flowDatabaseHelper.getUserDao();
            QueryBuilder<User, String> queryBuilder =
                    userDao.queryBuilder();
            userFriends = userDao.query(queryBuilder.where().eq(User.IS_ME, false).prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        UserFriends users = new UserFriends();
        users.setFriends(new ArrayList<User>(userFriends));
        return users;
    }
}
