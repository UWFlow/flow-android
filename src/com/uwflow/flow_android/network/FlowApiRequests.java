package com.uwflow.flow_android.network;

import android.content.Context;
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
    public static void login(String facebookId, String facebookAccessToken, final Context context) {
        RequestParams params = new RequestParams();
        params.put(Constants.FBID, facebookId);
        params.put(Constants.FACEBOOK_ACCESS_TOKEN, facebookAccessToken);

        FlowAsyncClient.post(Constants.API_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Successfully got a response
                Log.d(Constants.UW_FLOW, "Login Success");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(Constants.UW_FLOW, "Failed");
            }
        });
    }

    public static void searchUser(final FlowApiRequestCallback callback) {
        final String uri = Constants.API_USER;
        searchDetails(uri, callback);
    }


    public static void searchUsers(String userId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_USERS_SEACH, userId);
        searchDetails(uri, callback);
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
        HashMap<String, String> headers = new HashMap<String, String>();
        Cookie cookie = FlowAsyncClient.getCookie();
        headers.put(Constants.HEADER_COOKIE, cookie.getName() + "=" +cookie.getValue() + ";");
        FlowAsyncClient.get(headers, uri, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Successfully got a response
                callback.onSuccess(parseData(responseBody));
            }

            public void onFailure(int statusCode, org.apache.http.Header[] headers, java.lang.String responseBody, java.lang.Throwable e) {
                Log.d(Constants.UW_FLOW, responseBody);
            }
        });
    }

    private static void searchDetails(String uri, final FlowApiRequestCallback callback) {
        FlowAsyncClient.get(uri, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Successfully got a response
                callback.onSuccess(parseData(responseBody));
            }

            @Override
            public void onSuccess(org.json.JSONObject response) {
                // Successfully got a response
                Log.d(Constants.UW_FLOW, response.toString());
            }
        });
    }

    private static JSONObject parseData(byte[] data) {
        try {
            String str = new String(data, "UTF-8");
            JSONArray ary = new JSONArray(str);
            JSONObject obj = new JSONObject();
            obj.put("course", ary);
            return obj;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String str = new String(data, "UTF-8");
            JSONObject obj = new JSONObject(str);
            return obj;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
