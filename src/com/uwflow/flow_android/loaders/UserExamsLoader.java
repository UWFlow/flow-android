package com.uwflow.flow_android.loaders;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.Exam;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserExamsLoader extends FlowAbstractDataLoader<List<Exam>> {
    public UserExamsLoader(Context context, FlowDatabaseHelper flowDatabaseHelper) {
        super(context, flowDatabaseHelper);
    }

    @Override
    protected List<Exam> loadDelegate() {
        List<Exam> exams = new ArrayList<Exam>();
        try {
            Dao<Exam, Integer> userExamDao = flowDatabaseHelper.getUserExamDao();
            exams = userExamDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }
}
