package com.uwflow.flow_android.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.Exam;
import com.uwflow.flow_android.db_object.Exams;
import com.uwflow.flow_android.fragment.ProfileFragment;

import java.util.List;

public class UserExamsLoaderCallback implements LoaderManager.LoaderCallbacks<Exams>{
    // This is the fragment that contains all other fragments
    protected Fragment parentFragment;
    protected Context context;
    protected FlowDatabaseHelper flowDatabaseHelper;

    public UserExamsLoaderCallback(Context context, Fragment parentFragment, FlowDatabaseHelper flowDatabaseHelper) {
        this.parentFragment = parentFragment;
        this.context = context;
        this.flowDatabaseHelper = flowDatabaseHelper;
    }

    @Override
    public Loader<Exams> onCreateLoader(int i, Bundle bundle) {
        return new UserExamsLoader(context, flowDatabaseHelper, parentFragment);
    }

    @Override
    public void onLoadFinished(Loader<Exams> examsLoader, Exams exams) {
        if (exams == null){
            return;
        }
        if (parentFragment instanceof ProfileFragment){
            final ProfileFragment profileFragment= (ProfileFragment) parentFragment;
            profileFragment.setUserExams(exams);
        }
    }

    @Override
    public void onLoaderReset(Loader<Exams> examsLoader) {

    }
}
