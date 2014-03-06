package com.uwflow.flow_android.constant;

public class Constants {
    public static final String BASE_URL = "https://uwflow.com/";
    public static final String COOKIE = "flowCookie";
    public static final String FBID = "fbid";
    public static final String FACEBOOK_ACCESS_TOKEN = "fb_access_token";
    public static final String SET_COOKIE = "set-cookie";
    public static final String HEADER_COOKIE = "Cookie";

    public static final String API_LOGIN = "api/v1/login/facebook";
    public static final String API_COURSE = "api/v1/courses/%s";
    public static final String API_COURSE_PROFESSORS = "api/v1/courses/%s/professors";
    public static final String API_COURSE_EXAMS = "api/v1/courses/%s/exams";
    public static final String API_COURSE_SECTIONS = "api/v1/courses/%s/sections";
    public static final String API_COURSE_USERS = "api/v1/courses/%s/users";
    public static final String API_USER = "api/v1/user";
    public static final String API_USERS = "api/v1/users/%s";
    public static final String API_USER_SCHEDULE = "api/v1/user/schedule";
    public static final String API_USERS_SCHEDULE = "api/v1/users/%s/schedule";
    public static final String API_USER_EXAMS = "api/v1/user/exams";
    public static final String API_USERS_EXAMS = "api/v1/users/%s/exams";
    public static final String API_USER_COURSES = "api/v1/user/courses";
    public static final String API_USERS_COURSES = "api/v1/users/%s/courses";
    public static final String API_USER_FRIENDS = "api/v1/user/friends";
    public static final String API_USERS_FRIENDS = "api/v1/users/%s/friends";
    public static final String API_SEARCH_COURSES = "api/v1/search/courses?%s";

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

    public static class API_REQUEST_CALL_ID {
        public static final int API_USER_COURSE = 0;
        public static final int API_USER_FRIENDS = 1;
        public static final int API_USER_EXAMS = 2;
        public static final int API_USER_SCHEDULE = 3;
        public static final int API_USER = 4;
        public static final int API_COURSE_SECTIONS = 5;
        public static final int API_COURSE_EXAMS = 6;
        public static final int API_COURSE_PROFESSORS = 7;
        public static final int API_COURSE_USERS = 8;
        public static final int API_COURSE = 9;
        public static final int API_SEARCH_COURSES = 10;
    }

    public static final String UW_FLOW = "uwflow";

    public static final String COURSE_ID_KEY = "course_id";
    public static final String PROFILE_ID_KEY = "profile_id";

    public static final int COURSE_SCHEDULE_PAGE_INDEX = 0;
    public static final int COURSE_ABOUT_PAGE_INDEX = 1;
    public static final int COURSE_REVIEWS_PAGE_INDEX = 2;

    public static final int PROFILE_FRIENDS_PAGE_INDEX = 0;
    public static final int PROFILE_SCHEDULE_PAGE_INDEX = 1;
    public static final int PROFILE_EXAMS_PAGE_INDEX = 2;
    public static final int PROFILE_COURSES_PAGE_INDEX = 3;

}
