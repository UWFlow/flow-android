package com.uwflow.flow_android.network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.*;
import com.uwflow.flow_android.util.JsonToDbUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

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

    public static void searchCourses(String keywords, String sortMode, Boolean excludeTakenCourses, Integer count,
                                     Integer offset, final FlowApiRequestCallback callback) {
        // Build the search query string
        Uri.Builder uriBuilder = new Uri.Builder();
        if (StringUtils.isNotBlank(keywords)) {
            uriBuilder.appendQueryParameter("keywords", keywords);
        }
        if (StringUtils.isNotEmpty(sortMode)) {
            uriBuilder.appendQueryParameter("sort_mode", sortMode);
        }
        if (excludeTakenCourses) {
            uriBuilder.appendQueryParameter("exclude_taken_courses", "yes");
        }
        if (count != null && count >= 0) {
            uriBuilder.appendQueryParameter("count", String.valueOf(count));
        }
        if (offset != null && offset >= 0) {
            uriBuilder.appendQueryParameter("offset", String.valueOf(offset));
        }

        final String encodedQuery = uriBuilder.build().getEncodedQuery();
        final String uri = String.format(Constants.API_SEARCH_COURSES, encodedQuery);

        getDetails(Constants.API_REQUEST_CALL_ID.API_SEARCH_COURSES, uri, callback);
    }

    public static void getUser(final FlowApiRequestCallback callback) {
        final String uri = Constants.API_USER;
        getDetails(uri, callback);
    }

    public static void getUserSchedule(final FlowApiRequestCallback callback) {
        final String uri = Constants.API_USER_SCHEDULE;
        getDetails(uri, callback);
    }

    public static void getUserExams(final FlowApiRequestCallback callback) {
        final String uri = Constants.API_USER_EXAMS;
        getDetails(uri, callback);
    }

    public static void getUserCourses(final FlowApiRequestCallback callback) {
        final String uri = Constants.API_USER_COURSES;
        getDetails(uri, callback);
    }

    public static void getUserFriends(final FlowApiRequestCallback callback) {
        final String uri = Constants.API_USER_FRIENDS;
        getDetails(uri, callback);
    }

    public static void getUser(String userId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_USERS, userId);
        getDetails(Constants.API_REQUEST_CALL_ID.API_USER, uri, callback);
    }

    public static void getUserSchedule(String userId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_USERS_SCHEDULE, userId);
        getDetails(Constants.API_REQUEST_CALL_ID.API_USER_SCHEDULE, uri, callback);
    }

    public static void getUserExams(String userId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_USERS_EXAMS, userId);
        getDetails(Constants.API_REQUEST_CALL_ID.API_USER_EXAMS, uri, callback);
    }

    public static void getUserCourses(String userId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_USERS_COURSES, userId);
        getDetails(Constants.API_REQUEST_CALL_ID.API_USER_COURSE, uri, callback);
    }

    public static void getUserFriends(String userId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_USERS_FRIENDS, userId);
        getDetails(Constants.API_REQUEST_CALL_ID.API_USER_FRIENDS, uri, callback);
    }


    public static void getCourse(String courseId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_COURSE, courseId);
        getDetails(Constants.API_REQUEST_CALL_ID.API_COURSE, uri, callback);
    }

    public static void getCourseProfessors(String courseId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_COURSE_PROFESSORS, courseId);
        getDetails(Constants.API_REQUEST_CALL_ID.API_COURSE_PROFESSORS, uri, callback);
    }

    public static void getCourseExams(String courseId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_COURSE_EXAMS, courseId);
        getDetails(Constants.API_REQUEST_CALL_ID.API_COURSE_EXAMS, uri, callback);
    }

    public static void getCourseSections(String courseId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_COURSE_SECTIONS, courseId);
        getDetails(Constants.API_REQUEST_CALL_ID.API_COURSE_SECTIONS, uri, callback);
    }

    public static void getCourseUsers(String courseId, final FlowApiRequestCallback callback) {
        final String uri = String.format(Constants.API_COURSE_USERS, courseId);
        getDetails(Constants.API_REQUEST_CALL_ID.API_COURSE_USERS, uri, callback);
    }

    private static void getAuthenticatedDetails(final int id, String uri, final FlowApiRequestCallback callback) {
        HashMap<String, String> headers = new HashMap<String, String>();
        Cookie cookie = FlowAsyncClient.getCookie();
        if (cookie == null) {
            // FIXME(david): Remove this hard-coded session cookie and make sure it's invalidated
            headers.put(Constants.HEADER_COOKIE, "session=\"BswubxSTbrmP+52mREbNSbAZ0Zk=?_csrf_token=UydENVVURDk1MU8zRktFNEcnCnAxCi4=&user_id=Y2NvcHlfcmVnCl9yZWNvbnN0cnVjdG9yCnAxCihjYnNvbi5vYmplY3RpZApPYmplY3RJZApwMgpjX19idWlsdGluX18Kb2JqZWN0CnAzCk50UnA0ClMnUVJROFx4ZDhceDlkYlx4MGVceGU2XHhjNG5ceDg0JwpwNQpiLg==\";");
        } else {
            headers.put(Constants.HEADER_COOKIE, cookie.getName() + "=" + cookie.getValue() + ";");
        }
        FlowAsyncClient.get(headers, uri, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(org.json.JSONObject response) {
                FlowNetworkAsyncParser parser = new FlowNetworkAsyncParser(callback, id);
                parser.execute(response);
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onFailure("Call failed code: " + statusCode);
            }
        });
    }

    private static void getAuthenticatedDetails(String uri, final FlowApiRequestCallback callback) {
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

    private static void getDetails(final int id, String uri, final FlowApiRequestCallback callback) {
        FlowAsyncClient.get(uri, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(org.json.JSONObject response) {
                FlowNetworkAsyncParser parser = new FlowNetworkAsyncParser(callback, id);
                parser.execute(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onFailure(null);
            }
        });
    }

    private static void getDetails(String uri, final FlowApiRequestCallback callback) {
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

    protected static class FlowNetworkAsyncParser extends AsyncTask<JSONObject, Void, Object> {
        protected int callId;
        protected FlowApiRequestCallback callback;

        public FlowNetworkAsyncParser(FlowApiRequestCallback callback, int callId) {
            this.callback = callback;
            this.callId = callId;
        }


        @Override
        protected Object doInBackground(JSONObject... jsonObjects) {
            JSONObject jsonObject = jsonObjects[0];
            switch (callId) {
                case Constants.API_REQUEST_CALL_ID.API_COURSE:
                    return JsonToDbUtil.getCourseDetail(jsonObject);

                case Constants.API_REQUEST_CALL_ID.API_COURSE_EXAMS:
                    return JsonToDbUtil.getCourseExams(jsonObject);

                case Constants.API_REQUEST_CALL_ID.API_COURSE_PROFESSORS:
                    return JsonToDbUtil.getCourseProfessors(jsonObject);

                case Constants.API_REQUEST_CALL_ID.API_COURSE_USERS:
                    return JsonToDbUtil.getCourseUserDetail(jsonObject);

                case Constants.API_REQUEST_CALL_ID.API_COURSE_SECTIONS:
                    return JsonToDbUtil.getCourseSections(jsonObject);

                case Constants.API_REQUEST_CALL_ID.API_USER:
                    return JsonToDbUtil.getUser(jsonObject);

                case Constants.API_REQUEST_CALL_ID.API_USER_COURSE:
                    return JsonToDbUtil.getUserCourseDetail(jsonObject);

                case Constants.API_REQUEST_CALL_ID.API_USER_EXAMS:
                    return JsonToDbUtil.getUserExams(jsonObject);

                case Constants.API_REQUEST_CALL_ID.API_USER_FRIENDS:
                    return JsonToDbUtil.getUserFriends(jsonObject);

                case Constants.API_REQUEST_CALL_ID.API_USER_SCHEDULE:
                    return JsonToDbUtil.getUserSchedule(jsonObject);

                case Constants.API_REQUEST_CALL_ID.API_SEARCH_COURSES:
                    return JsonToDbUtil.getSearchResults(jsonObject);

                // TODO(david): Handle default case which throws an exception
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(Object result) {
            try {
                switch (callId) {
                    case Constants.API_REQUEST_CALL_ID.API_COURSE:
                        callback.getCourseCallback((CourseDetail) result);
                        break;
                    case Constants.API_REQUEST_CALL_ID.API_COURSE_EXAMS:
                        callback.getCourseExamsCallback((Exams) result);
                        break;
                    case Constants.API_REQUEST_CALL_ID.API_COURSE_PROFESSORS:
                        callback.getCourseProfessorCallback((Professors) result);
                        break;
                    case Constants.API_REQUEST_CALL_ID.API_COURSE_USERS:
                        callback.getCourseUsersCallback((CourseUserDetail) result);
                        break;
                    case Constants.API_REQUEST_CALL_ID.API_COURSE_SECTIONS:
                        callback.getCourseSectionsCallback((Sections) result);
                        break;
                    case Constants.API_REQUEST_CALL_ID.API_USER:
                        callback.getUserCallback((User) result);
                        break;
                    case Constants.API_REQUEST_CALL_ID.API_USER_FRIENDS:
                        callback.getUserFriendsCallback((UserFriends) result);
                        break;
                    case Constants.API_REQUEST_CALL_ID.API_USER_SCHEDULE:
                        callback.getUserScheduleCallback((ScheduleCourses) result);
                        break;
                    case Constants.API_REQUEST_CALL_ID.API_USER_EXAMS:
                        callback.getUserExamsCallback((Exams) result);
                        break;
                    case Constants.API_REQUEST_CALL_ID.API_USER_COURSE:
                        callback.getUserCoursesCallback((UserCourseDetail) result);
                        break;
                    case Constants.API_REQUEST_CALL_ID.API_SEARCH_COURSES:
                        callback.searchCoursesCallback((SearchResults) result);
                        break;
                    default:
                        callback.onSuccess((JSONObject) result);
                }

            } catch (Exception e) {
                callback.onFailure("Failed to parse");
            }
        }
    }
}
