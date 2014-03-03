package com.uwflow.flow_android.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.entities.CourseInfo;
import com.uwflow.flow_android.entities.CourseOverallRating;
import com.uwflow.flow_android.entities.CourseReview;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasperfung on 3/2/14.
 */
public class CourseInfoLoader extends AsyncTaskLoader<CourseInfo> {
    private CourseInfo mCourseInfo;
    public static final String SERVER_URL = "https://uwflow.com/api/v1/courses/psych101";

    public CourseInfoLoader(Context context) {
        super(context);
    }

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    public static String connect(String url)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            //Log.i(TAG,response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                instream.close();
                return result;
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        return null;
    }


    /**
     * This is where the bulk of our work is done.  This function is
     * called in a background thread and should generate a new set of
     * data to be published by the loader.
     */
    @Override public CourseInfo loadInBackground() {
        String JsonResponse = connect(SERVER_URL);
        JSONObject json = null;
        try {
            json = new JSONObject(JsonResponse);
        } catch (JSONException e) {
            Log.e("BUGGER", "error creating JSONObject: " + e);
        }

        if (json == null) {
            return null;
        }

        CourseOverallRating courseOverallRating;

        try {
            // get course info
            String courseID = json.getString("id");
            String courseCode = json.getString("code");
            String courseName = json.getString("name");
            String courseDescription = json.getString("description");

            // get overall rating
            JSONObject overallObject = json.getJSONObject("overall");
            int overallCount = overallObject.getInt("count");
            double overallRating = overallObject.getDouble("rating");

            // get easy and useful ratings
            JSONArray ratingsArray = json.getJSONArray("ratings");
            JSONObject usefulObject = (JSONObject)ratingsArray.get(0);
            JSONObject easyObject = (JSONObject)ratingsArray.get(1);
            int usefulCount = usefulObject.getInt("count");
            double usefulRating = usefulObject.getDouble("rating");
            int easyCount = easyObject.getInt("count");
            double easyRating = easyObject.getDouble("rating");

            // get course reviews
            ArrayList<CourseReview> reviewsArrayList = new ArrayList<CourseReview>();
            JSONArray reviewsArray = json.getJSONArray("reviews");
            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject row = reviewsArray.getJSONObject(i);

                JSONObject author = row.getJSONObject("author");
                String name = null;
                String fbid = null;
                String picURL = null;
                if (author.has("program_name")) {
                    name = String.format("A %s student", author.getString("program_name"));
                } else {
                    name = author.getString("name");
                    fbid = author.getString("id");
                    // TODO: we need to include the fbid into each CourseReview object to provide a link to the users' profiles
                    picURL = author.getString("profile_pic_url");
                }
                String date = row.getString("comment_date");
                String comment = row.getString("comment");
                Bitmap image = getFacebookProfilePicture(picURL);

                JSONArray ratings = row.getJSONArray("ratings");
                Boolean useful = ((JSONObject)ratings.get(0)).isNull("rating") ? null : ((JSONObject)ratings.get(0)).getDouble("rating") == 1;
                Boolean easy = ((JSONObject)ratings.get(1)).isNull("rating") ? null : ((JSONObject)ratings.get(1)).getDouble("rating") == 1;
                Boolean likedIt = ((JSONObject)ratings.get(2)).isNull("rating") ? null : ((JSONObject)ratings.get(2)).getDouble("rating") == 1;

                reviewsArrayList.add(new CourseReview(name, date, comment, image, useful, easy, likedIt));
            }

            courseOverallRating = new CourseOverallRating(overallCount,
                    overallRating,
                    usefulCount,
                    usefulRating,
                    easyCount,
                    easyRating);

            mCourseInfo = new CourseInfo(
                    courseID,
                    courseCode,
                    courseName,
                    courseDescription,
                    courseOverallRating,
                    reviewsArrayList);
        } catch (JSONException e) {
            Log.e("BUGGER", "error creating CourseInfo from fetched JSONObject: " + e);
        }

        return mCourseInfo;
    }

//    /**
//     * Called when there is new data to deliver to the client.  The
//     * super class will take care of delivering it; the implementation
//     * here just adds a little more logic.
//     */
//    @Override public void deliverResult(List<AppEntry> apps) {
//        if (isReset()) {
//            // An async query came in while the loader is stopped.  We
//            // don't need the result.
//            if (apps != null) {
//                onReleaseResources(apps);
//            }
//        }
//        List<AppEntry> oldApps = mApps;
//        mApps = apps;
//
//        if (isStarted()) {
//            // If the Loader is currently started, we can immediately
//            // deliver its results.
//            super.deliverResult(apps);
//        }
//
//        // At this point we can release the resources associated with
//        // 'oldApps' if needed; now that the new result is delivered we
//        // know that it is no longer in use.
//        if (oldApps != null) {
//            onReleaseResources(oldApps);
//        }
//    }

    /**
     * Handles a request to start the Loader.
     */
    @Override protected void onStartLoading() {
        if (mCourseInfo != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mCourseInfo);
        }

        // TODO: Start watching for changes in the app data.
//        if (mPackageObserver == null) {
//            mPackageObserver = new PackageIntentReceiver(this);
//        }

        if (takeContentChanged() || mCourseInfo == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override public void onCanceled(CourseInfo courseInfo) {
        super.onCanceled(courseInfo);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(courseInfo);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (mCourseInfo != null) {
            onReleaseResources(mCourseInfo);
            mCourseInfo = null;
        }

        // TODO: Stop monitoring for changes (if we're monitoring in the first place).
//        if (mPackageObserver != null) {
//            getContext().unregisterReceiver(mPackageObserver);
//            mPackageObserver = null;
//        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(CourseInfo apps) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }

    public static Bitmap getFacebookProfilePicture(String url){
        if (url != null) {
            try {
                //            URL imageURL = new URL("http://graph.facebook.com/" + userID + "/picture?type=large");
                //            Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
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
}
