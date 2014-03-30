package com.uwflow.flow_android;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.uwflow.flow_android.network.FlowAsyncClient;
import org.json.JSONObject;



public class FlowApplication extends Application {

    public static final boolean isBlackberry = System.getProperty("os.name").equals("qnx");

    private static final String IS_USER_LOGGED_IN_KEY = "is_user_logged_in";
    private static final String MIXPANEL_TOKEN = "0a5e88bd3f288fe8a2d8adf94b452212";

    private boolean mIsUserLoggedIn = false;
    private MixpanelAPI mMixpanel = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Crashlytics.start(this);

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

        getMixpanel();
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

    /**
     * @return A reference to the singleton Mixpanel API tracker object.
     */
    public MixpanelAPI getMixpanel() {
        if (mMixpanel == null) {
            mMixpanel = MixpanelAPI.getInstance(getApplicationContext(), MIXPANEL_TOKEN);
        }
        return mMixpanel;
    }

    /**
     * Records an event and sends to analytics loggers.
     * @param eventName
     */
    public void track(String eventName) {
        track(eventName, new JSONObject());
    }

    /**
     * Records an event and sends to analytics loggers.
     * @param eventName
     * @param properties Additional parameters for this event.
     */
    public void track(String eventName, JSONObject properties) {
        getMixpanel().track("Android: " + eventName, properties);
    }
}
