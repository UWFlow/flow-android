package com.uwflow.flow_android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.uwflow.flow_android.fragment.*;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
    private static final String[] TITLES = new String[] {
            "Friends",
            "Schedule",
            "Exams",
            "Courses"
    };

    public ProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 : return new ProfileFriendFragment();
            case 1 : return new ProfileScheduleFragment();
            case 2 : return new ProfileExamFragment();
            case 3 : return new ProfileCourseFragment();
            default: return new AboutFragment();
        }
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