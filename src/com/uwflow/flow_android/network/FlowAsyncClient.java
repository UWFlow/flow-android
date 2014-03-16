package com.uwflow.flow_android.network;

import android.content.Context;
import com.loopj.android.http.*;
import com.uwflow.flow_android.constant.Constants;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.HashMap;
import java.util.List;

public class FlowAsyncClient {
    protected static AsyncHttpClient client = new AsyncHttpClient();

    public static void init(Context c) {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(c);
        client.setCookieStore(myCookieStore);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
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

    public static void storeCookie(String cookie) {
        PersistentCookieStore myCookieStore = (PersistentCookieStore) client.getHttpContext().getAttribute(ClientContext.COOKIE_STORE);
        BasicClientCookie newCookie = new BasicClientCookie(Constants.COOKIE, cookie);
        myCookieStore.addCookie(newCookie);
    }

    public static Cookie getCookie() {
        PersistentCookieStore myCookieStore = (PersistentCookieStore) client.getHttpContext().getAttribute(ClientContext.COOKIE_STORE);
        List<Cookie> cookies = myCookieStore.getCookies();
        if (!cookies.isEmpty()) {
            return cookies.get(0);
        } else {
            return null;
        }
    }

    public static void clearCookie() {
        PersistentCookieStore myCookieStore = (PersistentCookieStore) client.getHttpContext().getAttribute(ClientContext.COOKIE_STORE);
        myCookieStore.clear();
    }
}
