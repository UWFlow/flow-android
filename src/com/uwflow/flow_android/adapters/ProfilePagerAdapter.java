package com.uwflow.flow_android.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.fragment.ProfileCourseFragment;
import com.uwflow.flow_android.fragment.ProfileExamFragment;
import com.uwflow.flow_android.fragment.ProfileFriendFragment;
import com.uwflow.flow_android.fragment.ProfileScheduleFragment;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
    private Bundle mBundle;
    protected Boolean mIsUserMe;
    private static final int USER_PROFILE_TAB_NUMBER = 4;
    private static final int USER_FRIEND_PROFILE_TAB_NUMBER = 3;

    private static final String[] TITLES = new String[]{
            "Schedule",
            "Courses",
            "Exams",
            "Friends",
    };

    public ProfilePagerAdapter(FragmentManager fm, Bundle bundle, boolean isUserMe) {
        super(fm);
        mBundle = bundle;
        if (mBundle == null) {
            mBundle = new Bundle();
        }
        mBundle.putBoolean("isUserMe", isUserMe);
        this.mIsUserMe = isUserMe;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case Constants.PROFILE_FRIENDS_PAGE_INDEX:
                fragment = new ProfileFriendFragment();
                break;
            case Constants.PROFILE_SCHEDULE_PAGE_INDEX:
                fragment = new ProfileScheduleFragment();
                break;
            case Constants.PROFILE_EXAMS_PAGE_INDEX:
                fragment = new ProfileExamFragment();
                break;
            case Constants.PROFILE_COURSES_PAGE_INDEX:
                fragment = new ProfileCourseFragment();
                break;
        }
        if (fragment != null) fragment.setArguments(mBundle);
        return fragment;
    }

    @Override
    public int getCount() {
        if (mIsUserMe) {
            return USER_PROFILE_TAB_NUMBER;
        } else {
            return USER_FRIEND_PROFILE_TAB_NUMBER;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}