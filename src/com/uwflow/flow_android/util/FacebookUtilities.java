package com.uwflow.flow_android.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import com.uwflow.flow_android.constant.Constants;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jasperfung on 2/28/14.
 */
public class FacebookUtilities {

    /**
     * Creates an intent to view a user on Facebook. Attempts to use the Facebook app if installed, falling back to web.
     * @param context
     * @param fbid The user's Facebook ID.
     * @return
     */
    public static Intent getOpenFBProfileIntent(Context context, long fbid) {
        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(String.format("fb://profile/%d", fbid)));
        } catch (Exception e) {
            e.printStackTrace();
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(String.format("https://www.facebook.com/profile.php?id=%d", fbid)));
        }
    }

    public static Bitmap getFacebookProfilePicture(String url){
        if (url != null) {
            try {
                URL imageURL = new URL(url);
                Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                return bitmap;
            } catch (MalformedURLException e) {
                Log.e(Constants.UW_FLOW, "Error creating image URL: " + e);
            } catch (IOException e) {
                Log.e(Constants.UW_FLOW, "Error opening connection during image fetch: " + e);
            }
        }
        return null;
    }

    public static void viewUserOnFacebook(Context context, long facebookId) {
        Intent profileIntent = FacebookUtilities.getOpenFBProfileIntent(context, facebookId);
        context.startActivity(profileIntent);
    }

    public static String getFirstWord(String string) {
        String firstWord = null;
        if(string.contains(" ")){
            firstWord= string.substring(0, string.indexOf(" "));
        }
        return firstWord;
    }
}
