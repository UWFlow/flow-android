package com.uwflow.flow_android.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uwflow.flow_android.db_object.*;
import org.json.JSONObject;

public class JsonToDbUtil {
    public static User getUser(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return new Gson().fromJson(gsonObject, User.class);
    }

    public static User getUserMe(JSONObject jsonObject) {
        User user = getUser(jsonObject);
        user.setMe(true);
        return user;
    }

    public static UserFriends getUserFriends(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return new Gson().fromJson(gsonObject, UserFriends.class);
    }

    public static UserCourseDetail getUserCourseDetail(JSONObject jsonObject) {
        JsonObject gsonCourses = getJsonObject(jsonObject);
        UserCourseDetail courseDetail = new Gson().fromJson(gsonCourses, UserCourseDetail.class);
        return courseDetail;
    }

    public static ScheduleCourses getUserSchedule(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return new Gson().fromJson(gsonObject, ScheduleCourses.class);
    }

    public static Exams getUserExams(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return new Gson().fromJson(gsonObject, Exams.class);
    }

    public static CourseDetail getCourseDetail(JSONObject jsonObject) {
        JsonObject gson = (JsonObject) new JsonParser().parse(jsonObject.toString());
        return new Gson().fromJson(gson, CourseDetail.class);
    }

    public static Professors getCourseProfessors(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return new Gson().fromJson(gsonObject, Professors.class);
    }


    public static Sections getCourseSections(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return new Gson().fromJson(gsonObject, Sections.class);
    }

    public static Exams getCourseExams(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return new Gson().fromJson(gsonObject, Exams.class);
    }

    public static CourseUserDetail getCourseUserDetail(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return new Gson().fromJson(gsonObject, CourseUserDetail.class);
    }

    private static JsonObject getJsonObject(JSONObject jsonObject) {
        return (JsonObject) new JsonParser().parse(jsonObject.toString());
    }
}
