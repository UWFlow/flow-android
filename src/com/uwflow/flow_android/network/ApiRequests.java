package com.uwflow.flow_android.network;

import android.content.Context;
import android.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uwflow.flow_android.constant.Constants;
import org.apache.http.Header;

public class ApiRequests {
    public static void login(String facebookId, String facebookAccessToken, final Context context) {
        RequestParams params = new RequestParams();
        params.put(Constants.FBID, facebookId);
        params.put(Constants.FACEBOOK_ACCESS_TOKEN, facebookAccessToken);

        FlowAsyncClient.post(Constants.API_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Successfully got a response
                Log.d(Constants.UW_FLOW, "Login Success");

                if (headers != null) {
                    for (Header h : headers) {
                        if (h.getName().equalsIgnoreCase(Constants.SET_COOKIE)) {
                            String cookie = h.getValue();
                            FlowAsyncClient.storeCookie(context, cookie);
                        }
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(Constants.UW_FLOW, "Failed:");
                try {
                    for (Header h : headers) {
                        Log.d(Constants.UW_FLOW, h.getName() + "," + h.getValue() + "\n");
                    }
                    String decoded = new String(responseBody, "UTF-8");
                    Log.d(Constants.UW_FLOW, decoded);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRetry() {
                // Request was retried
                Log.d(Constants.UW_FLOW, "Retried");
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                Log.d(Constants.UW_FLOW, "In progress");
            }

            @Override
            public void onFinish() {
                // Completed the request (either success or failure)

                Log.d(Constants.UW_FLOW, "Finished");
            }
        });
    }
}
