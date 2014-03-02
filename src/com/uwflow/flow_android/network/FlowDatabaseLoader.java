package com.uwflow.flow_android.network;

import android.content.Context;
import android.os.AsyncTask;
import com.j256.ormlite.dao.Dao;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.Course;
import com.uwflow.flow_android.db_object.Exam;
import com.uwflow.flow_android.db_object.ScheduleCourse;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.util.JsonToDbUtil;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This method fetches data from the network and populates the database
 */
public class FlowDatabaseLoader {
    protected FlowDatabaseHelper flowDatabaseHelper;
    protected Context context;

    public FlowDatabaseLoader(Context context, FlowDatabaseHelper flowDatabaseHelper) {
        this.context = context;
        this.flowDatabaseHelper = flowDatabaseHelper;
    }

    public void loadOrReloadProfileData(ResultCollectorCallback callback) {
        FlowResultCollector resultCollector = new FlowResultCollector(5, callback);
        reloadUserMe(0, resultCollector);
        reloadProfileCourses(1, resultCollector);
        reloadProfileExams(2, resultCollector);
        reloadProfileFriends(3, resultCollector);
        reloadProfileSchedule(4, resultCollector);
    }

    public void reloadUserMe(final int index, final FlowResultCollector flowResultCollector){
        FlowApiRequests.searchUser(new FlowApiRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                new AsyncTask<JSONObject, Void, Object>(){

                    @Override
                    protected Void doInBackground(JSONObject... jsonObjects) {
                        try {
                            Dao<User, String> userDao = flowDatabaseHelper.getUserDao();
                            userDao.createOrUpdate(JsonToDbUtil.getUser(jsonObjects[0]));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object obj) {
                        handleCallback(index, flowResultCollector);
                    }
                }.execute(response);
            }

            @Override
            public void onFailure(String error) {
                handleCallback(index, flowResultCollector);
            }
        });
    }

    public void reloadProfileFriends(final int index, final FlowResultCollector flowResultCollector){
        FlowApiRequests.searchUserFriends(new FlowApiRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                new AsyncTask<JSONObject, Void, Object>(){
                    @Override
                    protected Void doInBackground(JSONObject... jsonObjects) {
                        ArrayList<User> userFriends = JsonToDbUtil.getUserFriends(jsonObjects[0]);
                        try {
                            Dao<User, String> userDao = flowDatabaseHelper.getUserDao();
                            for (User u : userFriends) {
                                userDao.createOrUpdate(u);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object obj) {
                        handleCallback(index, flowResultCollector);
                    }
                }.execute(response);
            }

            @Override
            public void onFailure(String error) {
                handleCallback(index, flowResultCollector);
            }
        });
    }

    public void reloadProfileSchedule(final int index, final FlowResultCollector flowResultCollector){
        FlowApiRequests.searchUserSchedule(new FlowApiRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                new AsyncTask<JSONObject, Void, Object>(){
                    @Override
                    protected Void doInBackground(JSONObject... jsonObjects) {
                        ArrayList<ScheduleCourse> scheduleCourses = JsonToDbUtil.getUserSchedule(jsonObjects[0]);
                        try {
                            Dao<ScheduleCourse, String> userCourseSchedule = flowDatabaseHelper.getUserScheduleCourseDao();
                            for (ScheduleCourse sc : scheduleCourses) {
                                userCourseSchedule.createOrUpdate(sc);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object obj) {
                        handleCallback(index, flowResultCollector);
                    }
                }.execute(response);
            }

            @Override
            public void onFailure(String error) {
                handleCallback(index, flowResultCollector);
            }
        });
    }

    public void reloadProfileExams(final int index, final FlowResultCollector flowResultCollector){
        FlowApiRequests.searchUserExams(new FlowApiRequestCallback() {

            @Override
            public void onSuccess(JSONObject response) {
                new AsyncTask<JSONObject, Void, Object>(){
                    @Override
                    protected Void doInBackground(JSONObject... jsonObjects) {
                        ArrayList<Exam> userExams = JsonToDbUtil.getUserExam(jsonObjects[0]);
                        try {
                            Dao<Exam, Integer> userExamDao = flowDatabaseHelper.getUserExamDao();
                            for (Exam exam : userExams) {
                                userExamDao.createOrUpdate(exam);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object obj) {
                        handleCallback(index, flowResultCollector);
                    }
                }.execute(response);
            }

            @Override
            public void onFailure(String error) {
                handleCallback(index, flowResultCollector);
            }
        });
    }

    public void reloadProfileCourses(final int index, final FlowResultCollector flowResultCollector){
        FlowApiRequests.searchUserCourses(new FlowApiRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                new AsyncTask<JSONObject, Void, Object>(){
                    @Override
                    protected Void doInBackground(JSONObject... jsonObjects) {
                        ArrayList<Course> userCourses = JsonToDbUtil.getUserCourses(jsonObjects[0]);
                        try {
                            Dao<Course, String> courseDao = flowDatabaseHelper.getUserCourseDao();
                            for (Course c : userCourses) {
                                courseDao.createOrUpdate(c);

                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object obj) {
                        handleCallback(index, flowResultCollector);
                    }
                }.execute(response);
            }

            @Override
            public void onFailure(String error) {
                handleCallback(index, flowResultCollector);
            }
        });
    }

    protected void handleCallback(int index, FlowResultCollector flowResultCollector){
        if (flowResultCollector != null){
            flowResultCollector.setState(index, true);
        }
    }
}
