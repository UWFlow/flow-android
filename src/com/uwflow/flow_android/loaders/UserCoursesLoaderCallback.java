package com.uwflow.flow_android.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.UserCourseDetail;
import com.uwflow.flow_android.fragment.CourseFragment;
import com.uwflow.flow_android.fragment.ProfileFragment;

public class UserCoursesLoaderCallback implements LoaderManager.LoaderCallbacks<UserCourseDetail>{
    // This is the fragment that contains all other fragments
    protected Fragment parentFragment;
    protected Context context;
    protected FlowDatabaseHelper flowDatabaseHelper;

    public UserCoursesLoaderCallback(Context context, Fragment parentFragment, FlowDatabaseHelper flowDatabaseHelper) {
        this.parentFragment = parentFragment;
        this.context = context;
        this.flowDatabaseHelper = flowDatabaseHelper;
    }

    @Override
    public Loader<UserCourseDetail> onCreateLoader(int i, Bundle bundle) {
        return new UserCoursesLoader(context, flowDatabaseHelper, parentFragment);
    }

    @Override
    public void onLoadFinished(Loader<UserCourseDetail> userCourseDetailLoader, UserCourseDetail userCourseDetail) {
        if (userCourseDetail == null)
            return;
        if (parentFragment instanceof ProfileFragment){
            final ProfileFragment profileFragment= (ProfileFragment) parentFragment;
            profileFragment.setUserCourses(userCourseDetail);
        }
        if (parentFragment instanceof CourseFragment) {
            final CourseFragment courseFragment = (CourseFragment) parentFragment;
            courseFragment.setUserCourseDetail(userCourseDetail);
        }
    }

    @Override
    public void onLoaderReset(Loader<UserCourseDetail> userCourseDetailLoader) {

    }
}
