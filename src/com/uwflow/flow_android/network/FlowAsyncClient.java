package com.uwflow.flow_android.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.uwflow.flow_android.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.cookie.Cookie;

import java.util.HashMap;
import java.util.List;

// TODO(david): This class is more suitable as a singleton (ensure initialization code is run, only one instance needed)
public class FlowAsyncClient {
    private static final String TAG = "FlowAsyncClient";
    private static final String CSRF_PREFERENCE_KEY = "csrf_token";

    protected static AsyncHttpClient client = new AsyncHttpClient();
    private static PersistentCookieStore cookieStore;
    private static SharedPreferences preferences;
    private static String csrfToken;

    public static void init(Context c) {
        cookieStore = new PersistentCookieStore(c);
        client.setCookieStore(cookieStore);

        preferences = PreferenceManager.getDefaultSharedPreferences(c);
        csrfToken = preferences.getString(CSRF_PREFERENCE_KEY, null);
        useCsrfToken();
    }

    private static void useCsrfToken() {
        if (StringUtils.isNotEmpty(csrfToken)) {
            client.addHeader("X-CSRF-Token", csrfToken);
        }
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void get(HashMap<String, String> headers, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        final AsyncHttpClient specialClient = new AsyncHttpClient();

        for (String key : headers.keySet()) {
            specialClient.addHeader(key, headers.get(key));
        }
        specialClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return Constants.BASE_URL + relativeUrl;
    }

    public static Cookie getSessionCookie() {
        if (cookieStore == null) {
            return null;
        }

        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie: cookies) {
            if (cookie.getDomain().equals(Constants.SESSION_COOKIE_DOMAIN) && cookie.getName().equals("session")) {
                return cookie;
            }
        }

        return null;
    }

    public static void clearCookie() {
        if (cookieStore != null) {
            cookieStore.clear();
        }
    }

    public static void setCsrfToken(String xsrfToken) {
        csrfToken = xsrfToken;
        useCsrfToken();

        if (preferences != null) {
            preferences.edit()
                    .putString(CSRF_PREFERENCE_KEY, csrfToken)
                    .commit();
        }
    }
}
