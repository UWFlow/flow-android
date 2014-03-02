package com.uwflow.flow_android.dao;

import com.uwflow.flow_android.db_object.Course;
import com.uwflow.flow_android.db_object.Exam;
import com.uwflow.flow_android.db_object.ScheduleCourse;
import com.uwflow.flow_android.db_object.User;

import java.util.List;

public class FlowDaoCallbackAdapter {

    public void getUserCallback(User user){}
    public void getUserScheduleCallback(List<ScheduleCourse> schedule){}
    public void getUserExamsCallback(List<Exam> exams){}
    public void getUserCoursesCallback(List<Course> courses){}
    public void getUserFriendsCallback(List<User> userFriends){}

}
