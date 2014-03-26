package com.uwflow.flow_android.loaders;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.ScheduleCourses;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.db_object.UserFriends;
import com.uwflow.flow_android.db_object.Users;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserFriendsLoader extends FlowAbstractDataLoader<UserFriends> {

    public UserFriendsLoader(Context context, FlowDatabaseHelper flowDatabaseHelper, Fragment baseFragment) {
        super(context, flowDatabaseHelper, baseFragment);
    }

    @Override
    protected UserFriends loadDelegate() {
        // we first check if we should load from database or from the network
        if (baseFragment != null) {
            final ProfileFragment profileFragment = ProfileFragment.convertFragment(baseFragment);
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
