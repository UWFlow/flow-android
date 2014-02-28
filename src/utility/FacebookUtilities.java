package utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by jasperfung on 2/28/14.
 */
public class FacebookUtilities {
    public static Intent getOpenFBProfileIntent(Context context, int fbid) {

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
}
