package com.uwflow.flow_android.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.fragment.*;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
    private Bundle mBundle;

    private static final String[] TITLES = new String[] {
            "Friends",
            "Schedule",
            "Exams",
            "Courses"
    };

    public ProfilePagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
	mBundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
	Fragment fragment = null;
        switch (position){
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
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }
}