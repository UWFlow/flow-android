package com.uwflow.flow_android.broadcast_receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.uwflow.flow_android.constant.Constants;

/**
 * This class generates Broadcasts and BroadcastReceivers
 */
public class BroadcastFactory {

    public static void fireProfileMeLoadedBroadcast(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.BroadcastActionId.PROFILE_DATABASE_USER_LOADED));
    }

    public static void fireProfileFriendLoadedBroadcast(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.BroadcastActionId.PROFILE_DATABASE_USER_FRIEND_LOADED));
    }

    public static void fireProfileScheduleLoadedBroadcast(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.BroadcastActionId.PROFILE_DATABASE_USER_SCHEDULE_LOADED));
    }

    public static void fireProfileExamLoadedBroadcast(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.BroadcastActionId.PROFILE_DATABASE_USER_EXAM_LOADED));

    }

    public static void fireProfileCoursesLoadedBroadcast(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.BroadcastActionId.PROFILE_DATABASE_USER_COURSE_LOADED));
    }


    public static void fireProfileMeFragmentDataLoaded(Context context) {
        Intent intent = new Intent(Constants.BroadcastActionId.UPDATE_PROFILE_USER);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void fireProfileFriendFragmentDataLoaded(Context context) {
        Intent intent = new Intent(Constants.BroadcastActionId.UPDATE_PROFILE_USER_FRIENDS);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void fireProfileScheduleFragmentDataLoaded(Context context) {
        Intent intent = new Intent(Constants.BroadcastActionId.UPDATE_PROFILE_USER_SCHEDULE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void fireProfileExamFragmentDataLoaded(Context context) {
        Intent intent = new Intent(Constants.BroadcastActionId.UPDATE_PROFILE_USER_EXAMS);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void fireProfileCourseFragmentDataLoaded(Context context) {
        Intent intent = new Intent(Constants.BroadcastActionId.UPDATE_PROFILE_USER_COURSES);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}
