package com.uwflow.flow_android.dao;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.uwflow.flow_android.db_object.Exam;
import com.uwflow.flow_android.db_object.User;

import java.sql.SQLException;
import java.util.*;

public class FlowDaoWrapper {
    protected FlowDatabaseHelper flowDatabaseHelper;
    protected Context context;

    public FlowDaoWrapper(Context context, FlowDatabaseHelper flowDatabaseHelper) {
        this.flowDatabaseHelper = flowDatabaseHelper;
        this.context = context;
    }

    public User getUser() {
        try {
            Dao<User, String> userDao = flowDatabaseHelper.getUserDao();
            QueryBuilder<User, String> queryBuilder =
                    userDao.queryBuilder();
            List<User> me = userDao.query(queryBuilder.where().eq(User.IS_ME, true).prepare());
            if (!me.isEmpty()){
                return me.get(0);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getUserFriends(){
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

    public List<Exam> getUserExams(){
        List<Exam> exams = new ArrayList<Exam>();
        try {
            Dao<Exam, Integer> userExamDao = flowDatabaseHelper.getUserExamDao();
            QueryBuilder<Exam, Integer> queryBuilder =
                    userExamDao.queryBuilder();
            exams = userExamDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }
}
