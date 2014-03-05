package com.uwflow.flow_android.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.fragment.ProfileFragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jasperfung on 2/28/14.
 */
public class FacebookUtilities {
    public static Intent getOpenFBProfileIntent(Context context, long fbid) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(String.format("fb://profile/%d", fbid)));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/planyourflow"));
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

    public static AlertDialog createUserDialog(final Context context, final User user, final int resId, final FragmentManager fragmentManager) {
        String firstName = user.getFirstName();

        final String[] dialogOptions = {
                String.format("View %s on Flow", firstName),
                String.format("View %s on Facebook", firstName)};

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("View Profile")
                .setItems(dialogOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 1:
                                // View friend on Facebook
                                Intent profileIntent =
                                        FacebookUtilities.getOpenFBProfileIntent(context, user.getFbid());
                                context.startActivity(profileIntent);
                                break;
                            case 0:
                            default:
                                // View friend on Flow

				Fragment profileFragment = new ProfileFragment();
				Bundle bundle = new Bundle();
				bundle.putString(Constants.PROFILE_ID_KEY, user.getId());
				profileFragment.setArguments(bundle);

				FragmentTransaction transaction = fragmentManager.beginTransaction(); // TODO: the fragmentmanager argument is final, this might be problematic
				transaction.replace(resId, profileFragment);
				transaction.addToBackStack(null);
				transaction.commit();
                                break;
                        }
                        dialog.dismiss();
                    }
                }).create();

        return dialog;
    }

    public static String getFirstWord(String string) {
        String firstWord = null;
        if(string.contains(" ")){
            firstWord= string.substring(0, string.indexOf(" "));
        }
        return firstWord;
    }
}
