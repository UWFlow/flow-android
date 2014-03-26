package com.uwflow.flow_android.loaders;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.dao.Dao;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.*;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserCoursesLoader extends FlowAbstractDataLoader<UserCourseDetail> {
    public UserCoursesLoader(Context context, FlowDatabaseHelper flowDatabaseHelper, Fragment baseFragment) {
        super(context, flowDatabaseHelper, baseFragment);
    }

    @Override
    protected UserCourseDetail loadDelegate() {
        //Check if we are loading the logged in user's courseDetail or user friend's course detail
        // For all user friend data we fetch from the network
        if (mBaseFragment != null) {
            final ProfileFragment profileFragment = ProfileFragment.convertFragment(mBaseFragment);
            if (profileFragment != null && profileFragment.getProfileID() != null) {
                // It seems like async client must be run from the Main ui thread
                mBaseFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FlowApiRequests.getUserCourses(
                                profileFragment.getProfileID(),
                                new FlowApiRequestCallbackAdapter() {
                                    @Override
                                    public void getUserCoursesCallback(UserCourseDetail userCourseDetail) {
                                        if (userCourseDetail == null) return;
                                        profileFragment.setUserCourses(userCourseDetail);
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                    }
                                });
                    }
                });
                return null;
            }
        }

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
