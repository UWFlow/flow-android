package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserCourseDetail {
    protected List<Course> courses = new ArrayList<Course>();

    @SerializedName("user_courses")
    protected List<UserCourse> userCourses = new ArrayList<UserCourse>();

    public List<UserCourse> getUserCourses() {
        return userCourses;
    }

    public void setUserCourses(List<UserCourse> userCourses) {
        this.userCourses = userCourses;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
