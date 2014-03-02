package com.uwflow.flow_android.network;

import android.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uwflow.flow_android.constant.Constants;
import org.apache.http.Header;
import org.apache.http.cookie.Cookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class FlowApiRequests {
    public static void login(String facebookId, String facebookAccessToken,
                             final FlowApiRequestCallback callback) {
        RequestParams params = new RequestParams();
        params.put(Constants.FBID, facebookId);
        params.put(Constants.FACEBOOK_ACCESS_TOKEN, facebookAccessToken);

        FlowAsyncClient.post(Constants.API_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Successfully got a response
                Log.d(Constants.UW_FLOW, "Login Success");
                callback.onSuccess(null);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(Constants.UW_FLOW, "Failed");
                callback.onFailure(null);
            }
        });
    }

    public static void searchUser(final FlowApiRequestCallback callback) {
        final String uri = Constants.API_USER;
        searchAuthenticatedDetails(uri, callback);
    }

    public static void searchUserSchedule(final FlowApiRequestCallback callback) {
        final String uri = Constants.API_USER_SCHEDULE;
        searchAuthenticatedDetails(uri, callback);
    }

    public static void searchUserExams(final FlowApiRequestCallback callback) {
        final String uri = Constants.API_USER_EXAMS;
        searchAuthenticatedDetails(uri, callback);
    }

    public static void searchUserCourses(final FlowApiRequestCallback callback) {
        final String uri = Constants.API_USER_COURSES;
        searchAuthenticatedDetails(uri, callback);
    }

    public static void searchUserFriends(final FlowApiRequestCallback callback) {
        final String uri = Constants.API_USER_FRIENDS;
        searchAuthenticatedDetails(uri, callback);
    }

    public static void searchUsers(String userId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_USERS_SEARCH, userId);
        searchAuthenticatedDetails(uri, callback);
    }

    public static void searchUsersSchedule(String userId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_USERS_SCHEDULE, userId);
        searchAuthenticatedDetails(uri, callback);
    }

    public static void searchUsersExams(String userId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_USERS_EXAMS, userId);
        searchAuthenticatedDetails(uri, callback);
    }

    public static void searchUsersCourses(String userId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_USERS_COURSES, userId);
        searchAuthenticatedDetails(uri, callback);
    }

    public static void searchUsersFriends(String userId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_USERS_FRIENDS, userId);
        searchAuthenticatedDetails(uri, callback);
    }


    public static void searchCourse(String courseId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_COURSE_SEARCH, courseId);
        searchDetails(uri, callback);
    }

    public static void searchCourseProfessors(String courseId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_COURSE_SEARCH_PROFESSORS, courseId);
        searchDetails(uri, callback);
    }

    public static void searchCourseExams(String courseId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_COURSE_SEARCH_EXAMS, courseId);
        searchDetails(uri, callback);
    }

    public static void searchCourseSections(String courseId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_COURSE_SEARCH_SECTIONS, courseId);
        searchDetails(uri, callback);
    }

    public static void searchCourseUsers(String courseId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_COURSE_SEARCH_USERS, courseId);
        searchAuthenticatedDetails(uri, callback);
    }

    private static void searchAuthenticatedDetails(String uri, final FlowApiRequestCallback callback) {
        HashMap<String, String> headers = new HashMap<String, String>();
        Cookie cookie = FlowAsyncClient.getCookie();
        if (cookie == null) {
            headers.put(Constants.HEADER_COOKIE, "session=\"BswubxSTbrmP+52mREbNSbAZ0Zk=?_csrf_token=UydENVVURDk1MU8zRktFNEcnCnAxCi4=&user_id=Y2NvcHlfcmVnCl9yZWNvbnN0cnVjdG9yCnAxCihjYnNvbi5vYmplY3RpZApPYmplY3RJZApwMgpjX19idWlsdGluX18Kb2JqZWN0CnAzCk50UnA0ClMnUVJROFx4ZDhceDlkYlx4MGVceGU2XHhjNG5ceDg0JwpwNQpiLg==\";");
        } else {
            headers.put(Constants.HEADER_COOKIE, cookie.getName() + "=" + cookie.getValue() + ";");
        }
        FlowAsyncClient.get(headers, uri, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(org.json.JSONObject response) {
                // Successfully got a response
                callback.onSuccess(response);
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onFailure(null);
            }
        });
    }

    private static void searchDetails(String uri, final FlowApiRequestCallback callback) {
        FlowAsyncClient.get(uri, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(org.json.JSONObject response) {
                // Successfully got a response
                callback.onSuccess(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onFailure(null);
            }
        });
    }
}
