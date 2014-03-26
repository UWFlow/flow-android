package com.uwflow.flow_android.loaders;

import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.dao.Dao;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.Exam;
import com.uwflow.flow_android.db_object.Exams;
import com.uwflow.flow_android.db_object.ScheduleCourses;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;

import java.sql.SQLException;
import java.util.List;

public class UserExamsLoader extends FlowAbstractDataLoader<Exams> {
    public UserExamsLoader(Context context, FlowDatabaseHelper flowDatabaseHelper, Fragment baseFragment) {
        super(context, flowDatabaseHelper, baseFragment);
    }

    @Override
    protected Exams loadDelegate() {
        // we first check if we should load from database or from the network
        if (baseFragment != null) {
            final ProfileFragment profileFragment = ProfileFragment.convertFragment(baseFragment);
            if (profileFragment != null && profileFragment.getProfileID() != null) {
                baseFragment.getActivity().runOnUiThread(new Runnable() {
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
