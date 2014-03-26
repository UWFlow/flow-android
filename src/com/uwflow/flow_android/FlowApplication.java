package com.uwflow.flow_android;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.uwflow.flow_android.network.FlowAsyncClient;

public class FlowApplication extends Application {
    private static final String IS_USER_LOGGED_IN_KEY = "is_user_logged_in";
    private boolean mIsUserLoggedIn = false;

    @Override
    public void onCreate() {
        super.onCreate();
        // Do init here
        FlowAsyncClient.init(getApplicationContext());
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences != null) {
            mIsUserLoggedIn = preferences.getBoolean(IS_USER_LOGGED_IN_KEY, false);
        }
    }

    public boolean isUserLoggedIn() {
        return mIsUserLoggedIn;
    }

    public void setUserLoggedIn(boolean isUserLoggedIn) {
        this.mIsUserLoggedIn = isUserLoggedIn;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences != null) {
            preferences.edit().putBoolean(IS_USER_LOGGED_IN_KEY, isUserLoggedIn).commit();
        }

    }
}
