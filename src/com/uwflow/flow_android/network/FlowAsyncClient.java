package com.uwflow.flow_android.network;

import android.content.Context;
import com.loopj.android.http.*;
import com.uwflow.flow_android.constant.Constants;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.List;

public class FlowAsyncClient {
    protected static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return Constants.BASE_URL + relativeUrl;
    }

    public static void storeCookie(Context c, String cookie) {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(c);
        client.setCookieStore(myCookieStore);
        BasicClientCookie newCookie = new BasicClientCookie(Constants.COOKIE, cookie);
        myCookieStore.addCookie(newCookie);
    }

    public static String getCookie(Context c) {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(c);
        List<Cookie> cookies = myCookieStore.getCookies();
        if (!cookies.isEmpty()) {
            return cookies.get(0).getValue();
        } else {
            return null;
        }
    }
}
