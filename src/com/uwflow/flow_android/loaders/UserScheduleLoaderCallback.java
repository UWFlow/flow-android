package com.uwflow.flow_android.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.ScheduleCourses;
import com.uwflow.flow_android.db_object.UserFriends;
import com.uwflow.flow_android.fragment.ProfileFragment;

public class UserScheduleLoaderCallback implements LoaderManager.LoaderCallbacks<ScheduleCourses>{
    // This is the fragment that contains all other fragments
    protected Fragment parentFragment;
    protected Context context;
    protected FlowDatabaseHelper flowDatabaseHelper;

    public UserScheduleLoaderCallback(Context context,Fragment parentFragment, FlowDatabaseHelper flowDatabaseHelper) {
        this.parentFragment = parentFragment;
        this.context = context;
        this.flowDatabaseHelper = flowDatabaseHelper;
    }

    @Override
    public Loader<ScheduleCourses> onCreateLoader(int i, Bundle bundle) {
        return new UserScheduleLoader(context, flowDatabaseHelper, parentFragment);
    }

    @Override
    public void onLoadFinished(Loader<ScheduleCourses> scheduleCoursesLoader, ScheduleCourses scheduleCourses) {
        if (scheduleCourses == null){
            return;
        }

        if (parentFragment instanceof ProfileFragment){
            ((ProfileFragment) parentFragment).setUserSchedule(scheduleCourses);
        }
    }

    @Override
    public void onLoaderReset(Loader<ScheduleCourses> scheduleCoursesLoader) {

    }
}
