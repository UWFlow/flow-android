package com.uwflow.flow_android.loaders;

import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;

import java.sql.SQLException;
import java.util.List;

public class UserLoader extends FlowAbstractDataLoader<User> {

    public UserLoader(Context context, FlowDatabaseHelper flowDatabaseHelper, Fragment baseFragment) {
        super(context, flowDatabaseHelper, baseFragment);
    }

    @Override
    protected User loadDelegate() {
        try {
            if (baseFragment != null) {
                final ProfileFragment profileFragment = ProfileFragment.convertFragment(baseFragment);
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
