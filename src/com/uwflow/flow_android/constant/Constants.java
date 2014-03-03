package com.uwflow.flow_android.constant;

public class Constants {
    public static final String BASE_URL = "https://uwflow.com/";
    public static final String COOKIE = "flowCookie";
    public static final String FBID = "fbid";
    public static final String FACEBOOK_ACCESS_TOKEN = "fb_access_token";
    public static final String SET_COOKIE = "set-cookie";
    public static final String HEADER_COOKIE = "Cookie";

    public static final String API_LOGIN = "api/v1/login/facebook";
    public static final String API_COURSE_SEARCH = "api/v1/courses/%s";
    public static final String API_COURSE_SEARCH_PROFESSORS = "api/v1/courses/%s/professors";
    public static final String API_COURSE_SEARCH_EXAMS = "api/v1/courses/%s/exams";
    public static final String API_COURSE_SEARCH_SECTIONS = "api/v1/courses/%s/sections";
    public static final String API_COURSE_SEARCH_USERS = "api/v1/courses/%s/users";
    public static final String API_USER = "api/v1/user";
    public static final String API_USERS_SEARCH = "api/v1/users/%s";
    public static final String API_USER_SCHEDULE = "api/v1/user/schedule";
    public static final String API_USERS_SCHEDULE = "api/v1/users/%s/schedule";
    public static final String API_USER_EXAMS = "api/v1/user/exams";
    public static final String API_USERS_EXAMS = "api/v1/users/%s/exams";
    public static final String API_USER_COURSES = "api/v1/user/courses";
    public static final String API_USERS_COURSES = "api/v1/users/%s/courses";
    public static final String API_USER_FRIENDS = "api/v1/user/friends";
    public static final String API_USERS_FRIENDS = "api/v1/users/%s/friends";


    public static class DatabaseColumnName{
        public static final String USER_ID = "user_id";

    }

    public static class LoaderManagerId {
        public static final int PROFILE_FRIENDS_LOADER_ID = 1;
        public static final int PROFILE_SCHEDULE_LOADER_ID = 2;
        public static final int PROFILE_EXAMS_LOADER_ID = 3;
        public static final int PROFILE_COURSES_LOADER_ID = 4;
        public static final int PROFILE_LOADER_ID = 5;
    }

    public static class API_REQUEST_CALL_ID{
        public static final int API_USER_COURSE_SEARCH = 0;
        public static final int API_USER_FRIENDS_SEARCH = 1;
        public static final int API_USER_EXAMS_SEARCH = 2;
        public static final int API_USER_SCHEDULE_SEARCH = 3;
        public static final int API_USER_SEARCH= 4;
        public static final int API_COURSE_SECTIONS_SEARCH = 5;
        public static final int API_COURSE_EXAMS_SEARCH = 6;
        public static final int API_COURSE_PROFESSORS_SEARCH = 7;
        public static final int API_COURSE_USERS_SEARCH= 8;
        public static final int API_COURSE_SEARCH = 9;
    }

    public static final String UW_FLOW = "uwflow";

    public static final int COURSE_SCHEDULE_PAGE_INDEX = 0;
    public static final int COURSE_ABOUT_PAGE_INDEX = 1;
    public static final int COURSE_REVIEWS_PAGE_INDEX = 2;

}
