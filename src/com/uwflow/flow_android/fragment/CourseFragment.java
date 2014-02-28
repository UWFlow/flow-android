package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.astuetz.PagerSlidingTabStrip;
import com.uwflow.flow_android.R;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseFragment extends Fragment {
    private View rootView;
    protected ViewPager mViewPager;
    protected PagerSlidingTabStrip mTabs;

    final private static int SCHEDULE_PAGE = 0;
    final private static int ABOUT_PAGE = 1;
    final private static int REVIEWS_PAGE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.course_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(new CoursePagerAdapter(getActivity().getSupportFragmentManager()));
        mTabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.pager_tabs);
        mTabs.setViewPager(mViewPager);

        // Set default tab to About
        mViewPager.setCurrentItem(ABOUT_PAGE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    private static class CoursePagerAdapter extends FragmentStatePagerAdapter {
        private static final String[] TITLES = new String[] {
                "Schedule",
                "About",
                "Reviews"
        };

        public CoursePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case SCHEDULE_PAGE : return new CourseScheduleFragment();
                case ABOUT_PAGE : return new CourseAboutFragment();
                case REVIEWS_PAGE : return new CourseReviewsFragment();
                default: return new AboutFragment();
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
