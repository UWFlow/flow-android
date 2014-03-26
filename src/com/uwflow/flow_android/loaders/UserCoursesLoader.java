package com.uwflow.flow_android.loaders;

import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.dao.Dao;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.Course;
import com.uwflow.flow_android.db_object.UserCourse;
import com.uwflow.flow_android.db_object.UserCourseDetail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserCoursesLoader extends FlowAbstractDataLoader<UserCourseDetail> {
    public UserCoursesLoader(Context context, FlowDatabaseHelper flowDatabaseHelper) {
        super(context, flowDatabaseHelper);
    }

    @Override
    protected UserCourseDetail loadDelegate() {
        UserCourseDetail userCourseDetail = new UserCourseDetail();
        try {
            List<Course> courses = new ArrayList<Course>();
            Dao<Course, String> userCourseDao = flowDatabaseHelper.getUserCourseDao();
            courses = userCourseDao.queryForAll();
            userCourseDetail.setCourses(courses);
            List<UserCourse> userCourses = new ArrayList<UserCourse>();
            Dao<UserCourse, String> userCourseStringDao = flowDatabaseHelper.getUserCourseExtraDao();
            userCourses = userCourseStringDao.queryForAll();
            userCourseDetail.setUserCourses(userCourses);
        } catch (SQLException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return userCourseDetail;
    }
}
