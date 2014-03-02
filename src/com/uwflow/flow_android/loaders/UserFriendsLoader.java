package com.uwflow.flow_android.loaders;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserFriendsLoader extends FlowAbstractDataLoader<List<User>> {

    public UserFriendsLoader(Context context, FlowDatabaseHelper flowDatabaseHelper) {
        super(context, flowDatabaseHelper);
    }

    @Override
    protected List<User> loadDelegate() {
        List<User> userFriends = new ArrayList<User>();
        try {
            Dao<User, String> userDao = flowDatabaseHelper.getUserDao();
            QueryBuilder<User, String> queryBuilder =
                    userDao.queryBuilder();
            userFriends = userDao.query(queryBuilder.where().eq(User.IS_ME, false).prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userFriends;
    }
}
