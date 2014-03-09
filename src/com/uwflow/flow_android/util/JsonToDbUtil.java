package com.uwflow.flow_android.util;

import com.google.gson.*;
import com.uwflow.flow_android.db_object.*;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;

public class JsonToDbUtil {

    // Our custom GSON deserializer that knows how to handle a Timestamp
    private static final Gson gson;
    static {
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapter(Timestamp.class, new TimestampDeserializer())
                .create();
    }

    public static User getUser(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return gson.fromJson(gsonObject, User.class);
    }

    public static User getUserMe(JSONObject jsonObject) {
        User user = getUser(jsonObject);
        user.setMe(true);
        return user;
    }

    public static UserFriends getUserFriends(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return gson.fromJson(gsonObject, UserFriends.class);
    }

    public static UserCourseDetail getUserCourseDetail(JSONObject jsonObject) {
        JsonObject gsonCourses = getJsonObject(jsonObject);
        UserCourseDetail courseDetail = gson.fromJson(gsonCourses, UserCourseDetail.class);
        return courseDetail;
    }

    public static ScheduleCourses getUserSchedule(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return gson.fromJson(gsonObject, ScheduleCourses.class);
    }

    public static Exams getUserExams(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return gson.fromJson(gsonObject, Exams.class);
    }

    public static CourseDetail getCourseDetail(JSONObject jsonObject) {
        JsonObject gsonObject = (JsonObject) new JsonParser().parse(jsonObject.toString());
        return gson.fromJson(gsonObject, CourseDetail.class);
    }

    public static Professors getCourseProfessors(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return gson.fromJson(gsonObject, Professors.class);
    }


    public static Sections getCourseSections(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return gson.fromJson(gsonObject, Sections.class);
    }

    public static Exams getCourseExams(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return gson.fromJson(gsonObject, Exams.class);
    }

    public static CourseUserDetail getCourseUserDetail(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return gson.fromJson(gsonObject, CourseUserDetail.class);
    }

    public static SearchResults getSearchResults(JSONObject jsonObject) {
        JsonObject gsonObject = getJsonObject(jsonObject);
        return gson.fromJson(gsonObject, SearchResults.class);
    }

    private static JsonObject getJsonObject(JSONObject jsonObject) {
        return (JsonObject) new JsonParser().parse(jsonObject.toString());
    }

    private static class TimestampDeserializer implements JsonDeserializer<Timestamp> {
        @Override
        public Timestamp deserialize(JsonElement jsonElement, Type type,
                                     JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Timestamp(jsonElement.getAsLong());
        }
    }

}
