package com.uwflow.flow_android.loaders;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.dao.Dao;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.ScheduleCourse;
import com.uwflow.flow_android.db_object.ScheduleCourses;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserScheduleLoader extends FlowAbstractDataLoader<ScheduleCourses> {


    public UserScheduleLoader(Context context, FlowDatabaseHelper flowDatabaseHelper) {
        super(context, flowDatabaseHelper);
    }

    @Override
    protected ScheduleCourses loadDelegate() {
        ScheduleCourses scheduleCourses = new ScheduleCourses();
        try {
            Dao<ScheduleCourse, String> userScheduleDao = flowDatabaseHelper.getUserScheduleCourseDao();
            List<ScheduleCourse> courses = userScheduleDao.queryForAll();
            scheduleCourses.setScheduleCourses(new ArrayList<ScheduleCourse>(courses));
            if (!courses.isEmpty()){
                scheduleCourses.setScreenshotUrl(courses.get(0).getScheduleUrl());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return scheduleCourses;
    }
}
