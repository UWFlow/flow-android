package com.uwflow.flow_android.network;

import com.uwflow.flow_android.db_object.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class FlowApiRequestCallback {
    abstract public void onSuccess(JSONObject response);
    abstract public void onFailure(String error);

    public void getUserCallback(User user) {
    }

    public void getUserScheduleCallback(List<ScheduleCourse> schedule) {
    }

    public void getUserExamsCallback(List<Exam> exams) {
    }

    public void getUserCoursesCallback(List<Course> courses) {
    }

    public void getUserFriendsCallback(List<User> userFriends) {
    }

    public void getCourseCallback(CourseDetailedWrapper courseDetailedWrapper) {
    }

    public void getCourseSectionCallback(ArrayList<Section> sections) {

    }

    public void getCourseUsersCallback(CourseUserDetails courseUserDetails) {

    }

    public void getCourseExamsCallback(ArrayList<Exam> exams) {

    }

    public void getCourseProfessorCallback(ArrayList<Professor> professors) {

    }
}
