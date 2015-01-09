package com.uwflow.flow_android.loaders;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.dao.Dao;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.ScheduleCourse;
import com.uwflow.flow_android.db_object.ScheduleCourses;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserScheduleLoader extends FlowAbstractDataLoader<ScheduleCourses> {
    private static final String TAG = UserScheduleLoader.class.getSimpleName();

    private LoaderUpdateReceiver UserScheduleLoadedReceiver;

    public UserScheduleLoader(Context context, FlowDatabaseHelper flowDatabaseHelper, Fragment baseFragment) {
        super(context, flowDatabaseHelper, baseFragment);
    }

    protected void registerReceiver(){
        super.registerReceiver();
        // Start watching for changes in the app data.
        if (UserScheduleLoadedReceiver == null) {
            UserScheduleLoadedReceiver = new LoaderUpdateReceiver(this, Constants.BroadcastActionId.PROFILE_DATABASE_USER_SCHEDULE_LOADED);
        }
    }

    protected void unregisterReceiver(){
        super.unregisterReceiver();
        if (UserScheduleLoadedReceiver != null) {
            LocalBroadcastManager.getInstance(this.getContext().getApplicationContext()).unregisterReceiver(UserScheduleLoadedReceiver);
            UserScheduleLoadedReceiver = null;
        }
    }

    @Override
    protected ScheduleCourses loadDelegate() {
        // we first check if we should load from database or from the network
        if (mBaseFragment != null) {
            final ProfileFragment profileFragment = (ProfileFragment) mBaseFragment;
            if (profileFragment != null && profileFragment.getProfileID() != null) {
                mBaseFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FlowApiRequests.getUserSchedule(
                                profileFragment.getProfileID(),
                                new FlowApiRequestCallbackAdapter() {
                                    @Override
                                    public void getUserScheduleCallback(ScheduleCourses scheduleCourses) {
                                        if (scheduleCourses == null) return;
                                        profileFragment.setUserSchedule(scheduleCourses);
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        Crashlytics.log(Log.ERROR, TAG,
                                                "Get user schedule API request failed: " + error);
                                    }
                                });
                    }
                });
                return null;
            }
        }

        // load from database for profile
        ScheduleCourses scheduleCourses = new ScheduleCourses();
        try {
            Dao<ScheduleCourse, String> userScheduleDao = flowDatabaseHelper.getUserScheduleCourseDao();
            List<ScheduleCourse> courses = userScheduleDao.queryForAll();
            scheduleCourses.setScheduleCourses(new ArrayList<ScheduleCourse>(courses));
            if (!courses.isEmpty()) {
                scheduleCourses.setScreenshotUrl(courses.get(0).getScheduleUrl());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return scheduleCourses;
    }
}
