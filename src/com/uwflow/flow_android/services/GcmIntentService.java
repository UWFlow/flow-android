package com.uwflow.flow_android.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.broadcast_receiver.GcmBroadcastReceiver;
import com.uwflow.flow_android.constant.Constants;

/**
 * Service that handles push notifications from GCM.
 */
public class GcmIntentService extends IntentService {
    private static final String TAG = GcmIntentService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 1;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                String errorMessage = "Push notification send error: " + extras.toString();
                Crashlytics.log(Log.ERROR, TAG, errorMessage);
                Log.e(TAG, errorMessage);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                String errorMessage = "Deleted push notifications on server: " + extras.toString();
                Crashlytics.log(Log.ERROR, TAG, errorMessage);
                Log.e(TAG, errorMessage);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Actually a regular message. Handle it.
                Log.i(TAG, "Received push notification " + extras.toString());
                displayNotification(extras);
            }
        }

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    /**
     * Notify a push notification received from GCM.
     * This is currently only configured to receive course class seat opening alerts.
     * @param data
     */
    private void displayNotification(Bundle data) {
        Intent resultIntent = new Intent(this, MainFlowActivity.class);
        resultIntent.putExtra(Constants.COURSE_ID_KEY, "cs241");  // FIXME(david): Don't hardcode

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of the app to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainFlowActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // TODO(david): Hook up text with data format from server
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Content title is here")
                .setContentText("This is the content text")
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}
