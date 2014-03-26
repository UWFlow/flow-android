package com.uwflow.flow_android.loaders;

import android.content.Context;
import com.crashlytics.android.Crashlytics;
import com.j256.ormlite.dao.Dao;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.Exam;
import com.uwflow.flow_android.db_object.Exams;

import java.sql.SQLException;
import java.util.List;

public class UserExamsLoader extends FlowAbstractDataLoader<Exams> {
    public UserExamsLoader(Context context, FlowDatabaseHelper flowDatabaseHelper) {
        super(context, flowDatabaseHelper);
    }

    @Override
    protected Exams loadDelegate() {
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
