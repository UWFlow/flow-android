package com.uwflow.flow_android.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
	    case 0 :
		fragment = new ProfileFriendFragment();
		break;
	    case 1 :
		fragment = new ProfileScheduleFragment();
		break;
	    case 2 :
		fragment = new ProfileExamFragment();
		break;
	    case 3 :
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