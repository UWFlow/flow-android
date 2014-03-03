package com.uwflow.flow_android.loaders;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.Course;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserCoursesLoader extends FlowAbstractDataLoader<List<Course>> {
    public UserCoursesLoader(Context context, FlowDatabaseHelper flowDatabaseHelper) {
        super(context, flowDatabaseHelper);
    }

    @Override
    protected List<Course> loadDelegate() {
        List<Course> courses = new ArrayList<Course>();
        try {
            Dao<Course, String> userCourseDao = flowDatabaseHelper.getUserCourseDao();
            courses = userCourseDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}
