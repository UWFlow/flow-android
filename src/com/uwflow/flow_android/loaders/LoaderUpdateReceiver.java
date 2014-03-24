package com.uwflow.flow_android.loaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import com.uwflow.flow_android.constant.Constants;

public class LoaderUpdateReceiver extends BroadcastReceiver {
    final Loader loader;

    public LoaderUpdateReceiver(Loader loader) {
        this.loader = loader;
        LocalBroadcastManager.getInstance(this.loader.getContext().getApplicationContext()).registerReceiver(this,
                new IntentFilter(Constants.BroadcastActionId.UPDATE_PROFILE_FROM_DATABASE));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        loader.onContentChanged();
    }
}
