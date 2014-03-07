package com.uwflow.flow_android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.CourseDetail;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseFragment extends Fragment {
    private static final String TAG = "CourseFragment";

    private String mCourseID;
    private CoursePagerAdapter mCoursePagerAdapter;

    private View rootView;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabs;
    private TextView mCourseCodeTextView;
    private TextView mCourseNameTextView;

    private CourseScheduleFragment mCourseScheduleFragment;
    private CourseAboutFragment mCourseAboutFragment;
    private CourseReviewsFragment mCourseReviewsFragment;

    /**
     * Static method to instantiate this class with arguments passed as a bundle.
     * @param courseId The ID of the course to show.
     * @return A new instance.
     */
    public static CourseFragment newInstance(String courseId) {
        CourseFragment courseFragment = new CourseFragment();

        Bundle args = new Bundle();
        args.putString(Constants.COURSE_ID_KEY, courseId);
        courseFragment.setArguments(args);

        return courseFragment;
    }

    // Only allow instantiation via the static method.
    private CourseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.course_layout, container, false);
        mCourseCodeTextView = (TextView) rootView.findViewById(R.id.course_code);
        mCourseNameTextView = (TextView) rootView.findViewById(R.id.course_name);
        mTabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.pager_tabs);

        mCourseScheduleFragment = new CourseScheduleFragment();
	mCourseScheduleFragment.setArguments(getArguments());
        mCourseAboutFragment = new CourseAboutFragment();
	mCourseAboutFragment.setArguments(getArguments());
        mCourseReviewsFragment = new CourseReviewsFragment();
	mCourseReviewsFragment.setArguments(getArguments());

	mCoursePagerAdapter = new CoursePagerAdapter(getChildFragmentManager(),
		mCourseScheduleFragment,
		mCourseAboutFragment,
		mCourseReviewsFragment);

	// TODO(david): Should show a spinner here while course info is loading
	Bundle args = getArguments();
	if (args != null) {
	    mCourseID = getArguments().getString(Constants.COURSE_ID_KEY);
	    fetchCourseInfo(mCourseID);
	} else {
	    Log.e(TAG, "CourseFragment created without bundle: cannot fetch course details.");
	}

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        // Note: this is sorta cheating. We might need to decrease this number so that we don't run into memory issues.
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mCoursePagerAdapter);
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


        final Button shareButton = (Button)rootView.findViewById(R.id.share_btn);
        if (mCourseID != null) {
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,
                            String.format("http://www.uwflow.com/course/%s", mCourseID));
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this course!");
                    startActivity(Intent.createChooser(shareIntent, "Share course"));
                }
            });
        } else {
            shareButton.setEnabled(false);
        }

	    return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
	super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
	super.onActivityCreated(savedInstanceState);
    }

    private void fetchCourseInfo(String course){

	FlowApiRequests.getCourse(
                course,
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getCourseCallback(CourseDetail courseDetail) {
                        mCourseCodeTextView.setText(courseDetail.getCode());
                        mCourseNameTextView.setText(courseDetail.getName());

                        mCourseAboutFragment.loadCourseInfo(courseDetail);
                        mCourseReviewsFragment.loadCourseInfo(courseDetail);

                    }
                });
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
