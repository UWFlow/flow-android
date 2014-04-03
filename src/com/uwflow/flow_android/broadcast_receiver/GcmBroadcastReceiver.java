package com.uwflow.flow_android.broadcast_receiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.uwflow.flow_android.services.GcmIntentService;

/**
 * Receives push notifications from GCM.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName componentName = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());

        // Start the handling service, keeping the device awake while it is launching.
        startWakefulService(context, intent.setComponent(componentName));
        setResultCode(Activity.RESULT_OK);
    }
}
