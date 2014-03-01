package com.uwflow.flow_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.astuetz.PagerSlidingTabStrip;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.constant.Constants;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseFragment extends Fragment {
    private View rootView;
    protected ViewPager mViewPager;
    protected PagerSlidingTabStrip mTabs;

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
        mViewPager.setCurrentItem(Constants.COURSE_ABOUT_PAGE_INDEX);

        Button shortlistButton = (Button)rootView.findViewById(R.id.shortlist_btn);
        shortlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add code to shortlist a course
            }
        });


        Button shareButton = (Button)rootView.findViewById(R.id.share_btn);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                // TODO: we need to construct a valid share-link
                shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.uwflow.com");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this course!");
                startActivity(Intent.createChooser(shareIntent, "Share"));
            }
        });
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
                case Constants.COURSE_SCHEDULE_PAGE_INDEX : return new CourseScheduleFragment();
                case Constants.COURSE_ABOUT_PAGE_INDEX: return new CourseAboutFragment();
                case Constants.COURSE_REVIEWS_PAGE_INDEX: return new CourseReviewsFragment();
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
