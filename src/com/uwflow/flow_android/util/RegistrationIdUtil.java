package com.uwflow.flow_android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.uwflow.flow_android.constant.Constants;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Helper methods for dealing with registration IDs used for push notifications from GCM.
 * Mostly adapted from http://developer.android.com/google/gcm/client.html
 */
public class RegistrationIdUtil {
    private static final String TAG = RegistrationIdUtil.class.getSimpleName();

    // Since this has to be shipped with APK, there is no need to keep this secret.
    // Also http://stackoverflow.com/questions/18196292/what-are-consequences-of-having-gcm-sender-id-being-exposed
    private static final String GCM_SENDER_ID = "914611125417";

    private static final String REGISTRATION_ID_KEY = "registration_id";

    private static GoogleCloudMessaging mGcm = null;
    private static String mRegistrationId = null;

    /**
     * Register this device with GCM to receive push notifications, if possible.
     * @param context
     */
    public static void init(Context context) {
        if (supportsGcm(context)) {
            mGcm = GoogleCloudMessaging.getInstance(context);
            mRegistrationId = getRegistrationId(context);

            if (StringUtils.isEmpty(mRegistrationId)) {
                registerInBackground(context);
            }
            Log.i(TAG, "Registration ID is " + mRegistrationId);
        } else {
            Log.w(TAG, "Google Play Services SDK not available. Cannot receive push notifications.");
        }
    }

    public static boolean supportsGcm(Context context) {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS;
    }

    /**
     * Get the current registration ID from shared preferences.
     *
     * @return The registration ID if available and valid; null otherwise.
     */
    public static String getRegistrationId(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String registrationId = preferences.getString(REGISTRATION_ID_KEY, null);

        if (StringUtils.isEmpty(registrationId)) {
            return registrationId;
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new app version.
        int registeredVersion = preferences.getInt(Constants.APP_VERSION_KEY, Integer.MIN_VALUE);
        int appVersion = getAppVersion(context);
        if (registeredVersion != appVersion) {
            return null;
        }

        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Crashlytics.logException(e);
            Log.e(TAG, "Could not get package name");
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Registers the application with the GCM servers asynchronously. Stores registration ID and app version code
     * in shared preferences.
     */
    private static void registerInBackground(final Context context) {
        if (mGcm == null) {
            return;
        }

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    mRegistrationId = mGcm.register(GCM_SENDER_ID);
                    storeRegistrationId(context, mRegistrationId);
                    Log.i(TAG, "New registration ID obtained: " + mRegistrationId);
                } catch (IOException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);

                    // TODO(david): Enter exponential backoff to retry register. Grr why can't Google just do this.
                }

                return null;
            }
        }.execute();
    }

    /**
     * Stores the registration ID and app versionCode in the application's {@code SharedPreferences}.
     */
    private static void storeRegistrationId(Context context, String registrationId) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int appVersion = getAppVersion(context);
        preferences.edit()
                .putString(REGISTRATION_ID_KEY, registrationId)
                .putInt(Constants.APP_VERSION_KEY, appVersion)
                .commit();
    }

}
