package com.uwflow.flow_android.loaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import com.uwflow.flow_android.constant.Constants;

/**
 * This loader class is used to listen for the refresh button. If pressed, notify all the
 * listeners
 */
public class LoaderUpdateReceiver extends BroadcastReceiver {
    final Loader loader;

    public LoaderUpdateReceiver(Loader loader, String... actions) {
        this.loader = loader;
        IntentFilter filter = new IntentFilter();

        for (int i = 0; i < actions.length; i++) {
            filter.addAction(actions[i]);
        }

        LocalBroadcastManager.getInstance(this.loader.getContext().getApplicationContext()).registerReceiver(this,
                filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        loader.onContentChanged();
    }
}
