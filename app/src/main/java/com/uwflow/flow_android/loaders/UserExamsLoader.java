package com.uwflow.flow_android.loaders;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.dao.Dao;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.Exam;
import com.uwflow.flow_android.db_object.Exams;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;

import java.sql.SQLException;
import java.util.List;

public class UserExamsLoader extends FlowAbstractDataLoader<Exams> {
    private final static String TAG = UserExamsLoader.class.getSimpleName();

    private LoaderUpdateReceiver userExamsLoadedReceiver;
    public UserExamsLoader(Context context, FlowDatabaseHelper flowDatabaseHelper, Fragment baseFragment) {
        super(context, flowDatabaseHelper, baseFragment);
    }

    protected void registerReceiver(){
        super.registerReceiver();
        // Start watching for changes in the app data.
        if (userExamsLoadedReceiver == null) {
            userExamsLoadedReceiver = new LoaderUpdateReceiver(this, Constants.BroadcastActionId.PROFILE_DATABASE_USER_EXAM_LOADED);
        }
    }

    protected void unregisterReceiver(){
        super.unregisterReceiver();
        if (userExamsLoadedReceiver != null) {
            LocalBroadcastManager.getInstance(this.getContext().getApplicationContext()).unregisterReceiver(userExamsLoadedReceiver);
            userExamsLoadedReceiver = null;
        }
    }

    @Override
    protected Exams loadDelegate() {
        // we first check if we should load from database or from the network
        if (mBaseFragment != null) {
            final ProfileFragment profileFragment = (ProfileFragment) mBaseFragment;
            if (profileFragment != null && profileFragment.getProfileID() != null) {
                mBaseFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FlowApiRequests.getUserExams(
                                profileFragment.getProfileID(),
                                new FlowApiRequestCallbackAdapter() {
                                    @Override
                                    public void getUserExamsCallback(Exams exams) {
                                        if (exams == null) return;
                                        profileFragment.setUserExams(exams);
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        Crashlytics.log(Log.ERROR, TAG, "Get user exams API request failed: " + error);
                                    }
                                });
                    }
                });
                return null;
            }
        }

        Exams exam = new Exams();
        try {
            Dao<Exam, Integer> userExamDao = flowDatabaseHelper.getUserExamDao();
            List<Exam> exams = userExamDao.queryForAll();
            exam.setExams(exams);
        } catch (SQLException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
        return exam;
    }
}
