package com.uwflow.flow_android.util;

import com.uwflow.flow_android.db_object.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class JsonToDbUtil {
    public static User getUser(JSONObject jsonObject){
        try {
            User user = new User();
            user.setId(jsonObject.getString("id"));
            user.setFirstName(jsonObject.getString("first_name"));
            user.setPoint(jsonObject.getInt("num_points"));
            user.setProgramName(jsonObject.getString("program_name"));
            user.setName(jsonObject.getString("name"));
            user.setLastName(jsonObject.getString("last_name"));
            user.setNumInvites(jsonObject.getInt("num_invites"));
            user.setFbid(jsonObject.getLong("fbid"));
            user.setMe(true);
            JSONObject imageUrls = jsonObject.getJSONObject("profile_pic_urls");
            String [] imageUrl = {imageUrls.getString("large"), imageUrls.getString("square"), imageUrls.getString("default")};
            user.setImages(imageUrl);
            return user;
        } catch (Exception e){
            // if we cant convert the data then just ignore it
            return null;
        }
    }

    public static ArrayList<User> getUserFriends(JSONObject jsonObject){
        ArrayList<User> userFriends = new ArrayList<User>();
        try {
            JSONArray friends = jsonObject.getJSONArray("friends");
            for (int i = 0; i < friends.length(); i++){
                User user = getUser(friends.getJSONObject(i));
                user.setMe(false);
                userFriends.add(user);
            }
        } catch (Exception e){
        }
        return userFriends;
    }

    private static ArrayList<Rating> getUserCourseRating(JSONArray ratings) throws JSONException {
        ArrayList <Rating> userRatings = new ArrayList<Rating>();
        for (int i = 0; i < ratings.length(); i++){
            JSONObject jsonRating = ratings.getJSONObject(i);
            Rating r = new Rating();
            r.setName(jsonRating.getString("name"));
            r.setCount(jsonRating.getInt("count"));
            r.setRating(jsonRating.getDouble("rating"));
            userRatings.add(r);
        }
        return userRatings;
    }

    private static ArrayList<String> getUserCourseProfessorIds(JSONArray professorIds) throws JSONException {
        ArrayList<String> profIds = new ArrayList<String>();
        for (int i = 0; i< professorIds.length(); i++){
            profIds.add(professorIds.getString(i));
        }
        return profIds;
    }

    public static ArrayList<Course> getUserCourses(JSONObject jsonObject){
        ArrayList<Course> userCourses = new ArrayList<Course>();
        try {
            JSONArray courses = jsonObject.getJSONArray("courses");
            for (int i = 0; i < courses.length(); i++){
                JSONObject jsonCourse = courses.getJSONObject(i);
                Course course = new Course();
                course.setId(jsonCourse.getString("id"));
                course.setRatings(getUserCourseRating(jsonCourse.getJSONArray("ratings")));
                course.setUserCourseId(jsonCourse.getString("user_course_id"));
                course.setCode(jsonCourse.getString("code"));
                course.setName(jsonCourse.getString("name"));
                course.setProfessorIds(getUserCourseProfessorIds(jsonCourse.getJSONArray("professor_ids")));
                course.setPrereqs(jsonCourse.getString("prereqs"));
                course.setOverallCount(jsonCourse.getJSONObject("overall").getInt("count"));
                course.setOverallRating(jsonCourse.getJSONObject("overall").getDouble("rating"));
                course.setDescription(jsonCourse.getString("description"));
                userCourses.add(course);
            }
        } catch (Exception e){
        }
        return userCourses;
    }

    public static ArrayList<ScheduleCourse> getUserSchedule(JSONObject jsonObject){

        ArrayList<ScheduleCourse> userSchedule = new ArrayList<ScheduleCourse>();
        try {
            JSONArray schedule = jsonObject.getJSONArray("schedule");
            for (int i = 0; i < schedule.length(); i++){
                JSONObject jsonScheduleCourse = schedule.getJSONObject(i);
                ScheduleCourse scheduleCourse = new ScheduleCourse();
                scheduleCourse.setId(jsonScheduleCourse.getString("id"));
                scheduleCourse.setProfId(jsonScheduleCourse.getString("prof_id"));
                scheduleCourse.setCourseId(jsonScheduleCourse.getString("course_id"));
                scheduleCourse.setBuilding(jsonScheduleCourse.getString("building"));
                scheduleCourse.setRoom(jsonScheduleCourse.getString("room"));
                scheduleCourse.setTermId(jsonScheduleCourse.getString("term_id"));
                scheduleCourse.setSectionType(jsonScheduleCourse.getString("section_type"));
                scheduleCourse.setSectionNum(jsonScheduleCourse.getString("section_num"));
                scheduleCourse.setClassNum(jsonScheduleCourse.getString("class_num"));
                scheduleCourse.setStartDate(new Date(jsonScheduleCourse.getLong("start_date")));
                scheduleCourse.setEndDate(new Date(jsonScheduleCourse.getLong("end_date")));
                userSchedule.add(scheduleCourse);
            }
        } catch (Exception e){
        }
        return userSchedule;
    }

    public static ArrayList<Exam> getUserExam(JSONObject jsonObject){
        ArrayList<Exam> userExams = new ArrayList<Exam>();
        try {
            JSONArray jsonUserExams = jsonObject.getJSONArray("exams");
            for (int i = 0; i < jsonUserExams.length(); i++){
                JSONObject jsonExam = jsonUserExams.getJSONObject(i);
                Exam exam = new Exam();
                exam.setUrl(jsonExam.getString("url"));
                exam.setInfoKnown(jsonExam.getBoolean("info_known"));
                exam.setLocation("location");
                exam.setCourseId(jsonExam.getString("course_id"));
                exam.setLocationKnown(jsonExam.getBoolean("location_known"));
                exam.setSections(jsonExam.getString("sections"));
                exam.setStartDate(new Date(jsonExam.getLong("start_date")));
                exam.setEndDate(new Date(jsonExam.getLong("end_date")));
                userExams.add(exam);
            }

        } catch (Exception e){
        }
        return userExams;
    }
}
