package com.uwflow.flow_android.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uwflow.flow_android.db_object.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class JsonToDbUtil {
    public static User getUser(JSONObject jsonObject) {
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
            JSONObject imageUrls = jsonObject.getJSONObject("profile_pic_urls");
            User.ProfilePicUrls urls = new User.ProfilePicUrls();
            urls.setDefaultPic(imageUrls.getString("default"));
            urls.setLarge(imageUrls.getString("large"));
            urls.setSquare(imageUrls.getString("square"));
            user.setProfilePicUrls(urls);
            return user;
        } catch (Exception e) {
            // if we cant convert the data then just ignore it
            return null;
        }
    }

    public static ArrayList<User> getUsers(JSONArray jsonUsers) throws JSONException {
        ArrayList<User> users = new ArrayList<User>();
        for (int i = 0; i < jsonUsers.length(); i++) {
            User user = getUser(jsonUsers.getJSONObject(i));
            user.setMe(false);
            users.add(user);
        }
        return users;
    }

    public static User getUserMe(JSONObject jsonObject) {
        User user = getUser(jsonObject);
        user.setMe(true);
        return user;
    }

    public static ArrayList<User> getUserFriends(JSONObject jsonObject) {
        ArrayList<User> userFriends = new ArrayList<User>();
        try {
            userFriends = getUsers(jsonObject.getJSONArray("friends"));
        } catch (Exception e) {
        }
        return userFriends;
    }

    private static ArrayList<Rating> getUserCourseRating(JSONArray ratings) throws JSONException {
        ArrayList<Rating> userRatings = new ArrayList<Rating>();
        for (int i = 0; i < ratings.length(); i++) {
            JSONObject jsonRating = ratings.getJSONObject(i);
            Rating r = new Rating();
            r.setName(jsonRating.getString("name"));
            if (jsonRating.has("count")) r.setCount(jsonRating.getInt("count"));
            r.setRating(jsonRating.getString("rating"));
            userRatings.add(r);
        }
        return userRatings;
    }

    private static ArrayList<String> getIds(JSONArray jsonIds) throws JSONException {
        ArrayList<String> ids = new ArrayList<String>();
        for (int i = 0; i < jsonIds.length(); i++) {
            ids.add(jsonIds.getString(i));
        }
        return ids;
    }

    public static Course getCourse(JSONObject jsonCourse) throws JSONException {
        Course course = new Course();
        course.setId(jsonCourse.getString("id"));
        course.setRatings(getUserCourseRating(jsonCourse.getJSONArray("ratings")));
        course.setUserCourseId(jsonCourse.getString("user_course_id"));
        course.setCode(jsonCourse.getString("code"));
        course.setName(jsonCourse.getString("name"));
        course.setProfessorIds(getIds(jsonCourse.getJSONArray("professor_ids")));
        course.setPrereqs(jsonCourse.getString("prereqs"));
        course.setOverallCount(jsonCourse.getJSONObject("overall").getInt("count"));
        course.setOverallRating(jsonCourse.getJSONObject("overall").getDouble("rating"));
        course.setDescription(jsonCourse.getString("description"));
        return course;
    }

    public static ArrayList<Course> getUserCourses(JSONObject jsonObject) {
        ArrayList<Course> userCourses = new ArrayList<Course>();
        try {
            JSONArray courses = jsonObject.getJSONArray("courses");
            for (int i = 0; i < courses.length(); i++) {
                userCourses.add(getCourse(courses.getJSONObject(i)));
            }
        } catch (Exception e) {
        }
        return userCourses;
    }

    public static ArrayList<ScheduleCourse> getUserSchedule(JSONObject jsonObject) {

        ArrayList<ScheduleCourse> userSchedule = new ArrayList<ScheduleCourse>();
        try {
            JSONArray schedule = jsonObject.getJSONArray("schedule");
            for (int i = 0; i < schedule.length(); i++) {
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
        } catch (Exception e) {
        }
        return userSchedule;
    }

    public static ArrayList<Exam> getUserExam(JSONObject jsonObject) {
        ArrayList<Exam> userExams = new ArrayList<Exam>();
        try {
            JSONArray jsonUserExams = jsonObject.getJSONArray("exams");
            for (int i = 0; i < jsonUserExams.length(); i++) {
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

        } catch (Exception e) {
        }
        return userExams;
    }

    protected static Author getReviewAuthor(JSONObject authorObj) throws JSONException {
        Author a = new Author();
        a.setProgramName(authorObj.getString("program_name"));
        return a;
    }

    protected static Review getReview(JSONObject jsonReview) throws JSONException {
        Review review = new Review();
        review.setComment(jsonReview.getString("comment"));
        review.setRatings(getUserCourseRating(jsonReview.getJSONArray("ratings")));
        review.setCommentDate(jsonReview.getLong("comment_date"));
        review.setPrivacy(jsonReview.getString("privacy"));
        if (jsonReview.has("author"))review.setAuthor(getReviewAuthor(jsonReview.getJSONObject("author")));
        return review;
    }

    protected static ArrayList<Review> getReviews(JSONArray jsonReviews) throws JSONException {
        ArrayList<Review> reviews = new ArrayList<Review>();
        for (int i = 0; i < jsonReviews.length(); i++) {
            reviews.add(getReview(jsonReviews.getJSONObject(i)));
        }
        return reviews;
    }

    protected static ArrayList<String> getProfessorIds(JSONArray jsonProfessorIds) throws JSONException {
        ArrayList<String> professorIds = new ArrayList<String>();
        for (int i = 0; i < jsonProfessorIds.length(); i++) {
            professorIds.add(jsonProfessorIds.getString(i));
        }
        return professorIds;
    }

    public static CourseDetailedWrapper getCourseDetail(JSONObject jsonObject) {
        JsonObject gson = (JsonObject)new JsonParser().parse(jsonObject.toString());
        Gson newgson = new Gson();
        CourseDetailedWrapper courseDetailedWrapper = newgson.fromJson(gson, CourseDetailedWrapper.class);
        return courseDetailedWrapper;
    }

//    public static ArrayList<Professor> getCourseProfessors(JSONObject jsonObject) {
//        ArrayList<Professor> professors = new ArrayList<Professor>();
//        try {
//            JSONArray jsonProfessors = jsonObject.getJSONArray("professors");
//            for (int i = 0; i < jsonProfessors.length(); i++) {
//                JSONObject jsonProfessor = jsonProfessors.getJSONObject(i);
//                Professor professor = new Professor();
//                professor.setRatings(getUserCourseRating(jsonProfessor.getJSONArray("course_ratings")));
//                professor.setReviews(getReviews(jsonProfessor.getJSONArray("course_reviews")));
//                professor.setId(jsonProfessor.getString("id"));
//                professor.setName(jsonProfessor.getString("name"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return professors;
//    }

    public static TermUser getTermUser(JSONObject jsonTermUser) throws JSONException {
        TermUser termUser = new TermUser();
        termUser.setTermId(jsonTermUser.getString("term_id"));
        termUser.setTermName(jsonTermUser.getString("term_name"));
        termUser.setUserIds(getIds(jsonTermUser.getJSONArray("user_ids")));
        return termUser;
    }

    public static ArrayList<TermUser> getTermUsers(JSONArray jsonTermUsers) throws JSONException {
        ArrayList<TermUser> termUsers = new ArrayList<TermUser>();
        for (int i = 0; i < jsonTermUsers.length(); i++){
            termUsers.add(getTermUser(jsonTermUsers.getJSONObject(i)));
        }
        return termUsers;
    }

    public static CourseUserDetails getCourseUsers(JSONObject jsonObject) {
        CourseUserDetails courseUserDetails = new CourseUserDetails();
        try {
            courseUserDetails.setUsers(getUsers(jsonObject.getJSONArray("users")));
            courseUserDetails.setTermUsers(getTermUsers(jsonObject.getJSONArray("term_users")));
        } catch (Exception e) {
        }
        return courseUserDetails;
    }



    public static ArrayList<Professor> getCourseProfessors(JSONObject jsonObject){
        JsonObject gson = (JsonObject)new JsonParser().parse(jsonObject.toString());
        JsonArray gsonCourseProfessors = gson.getAsJsonArray("professors");
        ArrayList<Professor> professors = new ArrayList<Professor>();
        for (int i = 0; i < gsonCourseProfessors.size(); i ++){
            JsonObject obj = gsonCourseProfessors.get(i).getAsJsonObject();
            Gson newgson = new Gson();
            Professor professor = newgson.fromJson(obj, Professor.class);
            professors.add(professor);
        }
        return professors;
    }



    public static ArrayList<Section> getCourseSections(JSONObject jsonObject){
        JsonObject gson = (JsonObject)new JsonParser().parse(jsonObject.toString());
        JsonArray gsonCourseSections = gson.getAsJsonArray("sections");
        ArrayList<Section> sections = new ArrayList<Section>();
        for (int i = 0; i < gsonCourseSections.size(); i ++){
            JsonObject obj = gsonCourseSections.get(i).getAsJsonObject();
            Gson newgson = new Gson();
            Section section = newgson.fromJson(obj, Section.class);
            sections.add(section);
        }
        return sections;
    }

    public static ArrayList<Exam> getCourseExams(JSONObject jsonObject){
        return getUserExam(jsonObject);
    }

}
