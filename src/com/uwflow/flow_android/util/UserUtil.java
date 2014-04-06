package com.uwflow.flow_android.util;

import android.content.Context;
import android.preference.PreferenceManager;
import com.uwflow.flow_android.constant.Constants;

public class UserUtil {

    /**
     * Save the logged in user's ID in shared preferences.
     */
    public static void saveLoggedInUserId(Context context, String userId) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(Constants.PROFILE_ID_KEY, userId)
                .commit();
    }

    /**
     * Get the logged in user's ID from shared preferences.
     */
    public static String getLoggedInUserId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.PROFILE_ID_KEY, null);
    }

}
