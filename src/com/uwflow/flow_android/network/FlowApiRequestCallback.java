package com.uwflow.flow_android.network;

import org.json.JSONObject;

public abstract class FlowApiRequestCallback {
    abstract public void onSuccess(JSONObject response);
    abstract public void onFailure(String error);
}
