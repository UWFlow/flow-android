package com.uwflow.flow_android.loaders;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.Course;
import com.uwflow.flow_android.db_object.ScheduleCourse;
import com.uwflow.flow_android.db_object.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserScheduleLoader extends FlowAbstractDataLoader<List<ScheduleCourse>> {

    public UserScheduleLoader(Context context, FlowDatabaseHelper flowDatabaseHelper) {
        super(context, flowDatabaseHelper);
    }

    @Override
    protected List<ScheduleCourse> loadDelegate() {
        List<ScheduleCourse> courses = new ArrayList<ScheduleCourse>();
        try {
            Dao<ScheduleCourse, String> userScheduleDao = flowDatabaseHelper.getUserScheduleCourseDao();
            courses = userScheduleDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}
