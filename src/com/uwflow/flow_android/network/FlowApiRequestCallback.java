package com.uwflow.flow_android.network;

import com.uwflow.flow_android.db_object.*;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class FlowApiRequestCallback {
    abstract public void onSuccess(JSONObject response);
    abstract public void onFailure(String error);

    public void getUserCallback(User user) {
    }

    public void getUserScheduleCallback(ScheduleCourses scheduleCourses) {
    }

    public void getUserExamsCallback(Exams exams) {
    }

    public void getUserCoursesCallback(UserCourseDetail userCourseDetail) {
    }

    public void getUserFriendsCallback(UserFriends userFriends) {
    }

    public void getCourseCallback(CourseDetail courseDetail) {
    }

    public void getCourseSectionCallback(Sections sections) {
    }

    public void getCourseUsersCallback(CourseUserDetail courseUserDetail) {
    }

    public void getCourseExamsCallback(Exams exams) {
    }

    public void getCourseProfessorCallback(Professors professors) {
    }
}
