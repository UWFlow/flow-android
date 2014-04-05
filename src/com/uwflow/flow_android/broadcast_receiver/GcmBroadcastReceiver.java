package com.uwflow.flow_android.broadcast_receiver;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
import com.uwflow.flow_android.constant.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Receives push notifications from GCM.
 *
 * This does not use a WakefulBroadcastReceiver and thus cannot take long to process. If more processing time
 * is needed, take a look at http://developer.android.com/google/gcm/client.html for extending WakefulBroadcastReceiver.
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = GcmBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
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
                displayNotification(context, extras);
            }
        }

        setResultCode(Activity.RESULT_OK);
    }

    /**
     * Notify a push notification received from GCM.
     * This is currently only configured to receive course class seat opening alerts.
     * @param data
     */
    private void displayNotification(Context context, Bundle data) {
        String courseName = "";
        String courseId = "";
        String courseCode = "";

        try {
            JSONObject course = new JSONObject(data.getString("course"));
            courseName = course.getString("name");
            courseId = course.getString("id");
            courseCode = course.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        // TODO(david): Look into why this doesn't go to the right course if Flow is already started
        Intent resultIntent = new Intent(context, MainFlowActivity.class);
        resultIntent.putExtra(Constants.COURSE_ID_KEY, courseId);

        // The stack builder object will contain an artificial back stack for the started Activity.
        // This ensures that navigating backward from the Activity leads out of the app to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainFlowActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        String title = String.format("%s: spots open!", courseCode);
        String text = String.format("%s: %s has seats open now.", courseCode, courseName);

        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(), notification);
    }

}
