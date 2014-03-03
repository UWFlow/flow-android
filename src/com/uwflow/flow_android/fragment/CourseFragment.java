package com.uwflow.flow_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.entities.CourseInfo;
import com.uwflow.flow_android.network.CourseInfoLoader;

import java.util.List;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<CourseInfo> {
    private View rootView;
    protected ViewPager mViewPager;
    protected PagerSlidingTabStrip mTabs;

    private TextView mCourseCodeTextView;
    private TextView mCourseNameTextView;
    private CourseInfo mCourseInfo;

    private CourseScheduleFragment mCourseScheduleFragment;
    private CourseAboutFragment mCourseAboutFragment;
    private CourseReviewsFragment mCourseReviewsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.course_layout, container, false);
        mCourseCodeTextView = (TextView) rootView.findViewById(R.id.course_code);
        mCourseNameTextView = (TextView) rootView.findViewById(R.id.course_name);
        mTabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.pager_tabs);

        mCourseScheduleFragment = new CourseScheduleFragment();
        mCourseAboutFragment = new CourseAboutFragment();
        mCourseReviewsFragment = new CourseReviewsFragment();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(new CoursePagerAdapter(getActivity().getSupportFragmentManager(), mCourseScheduleFragment, mCourseAboutFragment, mCourseReviewsFragment));
        mTabs.setViewPager(mViewPager);

        // Set default tab to About
        mViewPager.setCurrentItem(Constants.COURSE_ABOUT_PAGE_INDEX);
//        mViewPager.setOffscreenPageLimit(2); // TODO: this is sorta cheating. We might need to decrease this number so that we don't run into memory issues.

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

        // Prepare the loader.  Either re-connect with an existing one, or start a new one
        getLoaderManager().initLoader(0, null, this); // TODO: .forceLoad(); ???
    }

    @Override
    public android.support.v4.content.Loader<CourseInfo> onCreateLoader(int i, Bundle bundle) {
        return new CourseInfoLoader(getActivity());
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<CourseInfo> courseInfoLoader, CourseInfo courseInfo) {
        if (courseInfo == null) return;

        mCourseInfo = courseInfo;

        mCourseCodeTextView.setText(courseInfo.getCode());
        mCourseCodeTextView.setGravity(Gravity.LEFT);

        mCourseNameTextView.setText(courseInfo.getName());
        mCourseNameTextView.setGravity(Gravity.LEFT);

        mCourseAboutFragment.loadCourseInfo(mCourseInfo);
        mCourseReviewsFragment.loadCourseInfo(mCourseInfo);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<CourseInfo> courseInfoLoader) {
        // TODO Auto-generated method stub

    }


    private static class CoursePagerAdapter extends FragmentStatePagerAdapter {
        Fragment fragment1, fragment2, fragment3;
        private static final String[] TITLES = new String[] {
                "Schedule",
                "About",
                "Reviews"
        };

        public CoursePagerAdapter(FragmentManager fm, Fragment fragment1, Fragment fragment2, Fragment fragment3) {
            super(fm);
            this.fragment1 = fragment1;
            this.fragment2 = fragment2;
            this.fragment3 = fragment3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case Constants.COURSE_SCHEDULE_PAGE_INDEX : return fragment1;
//                    if (mCourseScheduleFragment == null) {
//                        courseScheduleFragment = new CourseScheduleFragment();
//                    };
//                    return courseScheduleFragment;
                case Constants.COURSE_ABOUT_PAGE_INDEX: return fragment2;
                case Constants.COURSE_REVIEWS_PAGE_INDEX: return fragment3;
                default: return fragment2;
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
