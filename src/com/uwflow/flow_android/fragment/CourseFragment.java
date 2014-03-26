package com.uwflow.flow_android.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.astuetz.PagerSlidingTabStrip;
import com.crashlytics.android.Crashlytics;
import com.uwflow.flow_android.FlowApplication;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.CourseDetail;
import com.uwflow.flow_android.db_object.UserCourse;
import com.uwflow.flow_android.db_object.UserCourseDetail;
import com.uwflow.flow_android.loaders.UserCoursesLoaderCallback;
import com.uwflow.flow_android.network.FlowApiRequestCallback;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;
import com.uwflow.flow_android.util.CourseUtil;
import com.uwflow.flow_android.nfc.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseFragment extends TrackedFragment implements SharableURL {
    private static final String TAG = "CourseFragment";

    private String mCourseID;
    private CoursePagerAdapter mCoursePagerAdapter;

    private View rootView;
    private ViewPager mViewPager;
    private PagerSlidingTabStrip mTabs;
    private TextView mCourseCodeTextView;
    private TextView mCourseNameTextView;
    private Button mShortlistButton;

    private CourseScheduleFragment mCourseScheduleFragment;
    private CourseAboutFragment mCourseAboutFragment;
    private CourseReviewsFragment mCourseReviewsFragment;

    private UserCourseDetail userCourseDetail;
    private CourseUpdateReceiver courseUpdateReceiver;

    /**
     * Static method to instantiate this class with arguments passed as a bundle.
     *
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mCourseScheduleFragment == null) {
            mCourseScheduleFragment = new CourseScheduleFragment();
            mCourseScheduleFragment.setArguments(getArguments());
        }
        if (mCourseAboutFragment == null) {
            mCourseAboutFragment = new CourseAboutFragment();
            mCourseAboutFragment.setArguments(getArguments());
        }
        if (mCourseReviewsFragment == null) {
            mCourseReviewsFragment = new CourseReviewsFragment();
            mCourseReviewsFragment.setArguments(getArguments());
        }
        courseUpdateReceiver = new CourseUpdateReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.course_layout, container, false);
        mCourseCodeTextView = (TextView) rootView.findViewById(R.id.course_code);
        mCourseNameTextView = (TextView) rootView.findViewById(R.id.course_name);
        mTabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.pager_tabs);

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
            String errorLog = "CourseFragment created without bundle: cannot fetch course details.";
            Log.e(TAG, errorLog);
            Crashlytics.log(Log.ERROR, TAG, errorLog);
        }

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        // Note: this is sorta cheating. We might need to decrease this number so that we don't run into memory issues.
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mCoursePagerAdapter);
        mTabs.setViewPager(mViewPager);

        // Set default tab to About
        mViewPager.setCurrentItem(Constants.COURSE_ABOUT_PAGE_INDEX);

        mShortlistButton = (Button) rootView.findViewById(R.id.shortlist_btn);
        final Button shareButton = (Button) rootView.findViewById(R.id.share_btn);

        if (!((FlowApplication)getActivity().getApplication()).isUserLoggedIn()) {
            mShortlistButton.setVisibility(View.GONE);

        }

        if (mCourseID != null) {
            final String humanizedCourseId = CourseUtil.humanizeCourseId(mCourseID);
            mShortlistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FlowApiRequests.addCourseToShortlist(mCourseID, new FlowApiRequestCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            String message = String.format("%s added to shortlist.", humanizedCourseId);
                            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            disableShortlistButton("Shortlisted");
                            // TODO(david): Also update the DB to reflect the change
                        }

                        @Override
                        public void onFailure(String error) {
                            String message = String.format("Failed to add %s to shortlist. :(", humanizedCourseId);
                            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });

                    JSONObject properties = new JSONObject();
                    try {
                        properties.put("course_id", mCourseID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    FlowApplication app = ((FlowApplication) getActivity().getApplication());
                    app.track("Add to shortlist", properties);
                    app.getMixpanel().getPeople().increment("Add to shortlist", 1);
                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FlowApplication) getActivity().getApplication()).track("Share course");

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT,
                            String.format("https://www.uwflow.com/course/%s", mCourseID));
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this course!");
                    startActivity(Intent.createChooser(shareIntent, "Share course"));
                }
            });
        } else {
            shareButton.setEnabled(false);
            mShortlistButton.setEnabled(false);
        }

        // Load user course info asynchronously
        final UserCoursesLoaderCallback userCoursesLoaderCallback = new UserCoursesLoaderCallback(
                getActivity().getApplicationContext(), this, ((MainFlowActivity) getActivity()).getHelper());
        getLoaderManager().initLoader(Constants.LoaderManagerId.COURSE_USER_COURSES_LOADER_ID, null,
                userCoursesLoaderCallback);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(courseUpdateReceiver,
                new IntentFilter(Constants.BroadcastActionId.UPDATE_CURRENT_FRAGMENT));
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).unregisterReceiver(courseUpdateReceiver);
        super.onPause();
    }

    @Override
    public String getUrl() {
        if (mCourseID == null) return null;
        return Constants.BASE_URL + Constants.URL_COURSE_EXT + mCourseID;
    }

    private void fetchCourseInfo(String course) {

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

    public void setUserCourseDetail(UserCourseDetail userCourseDetail) {
        this.userCourseDetail = userCourseDetail;

        // Change the shortlist button if the user has already taken/will take this course.
        for (UserCourse userCourse : userCourseDetail.getUserCourses()) {
            if (userCourse.getCourseId().equals(mCourseID)) {
                if (userCourse.getTermId().equals(Constants.SHORTLIST_TERM_ID)) {
                    disableShortlistButton("Shortlisted");
                } else {
                    disableShortlistButton(userCourse.getTermName());
                }
                break;
            }
        }
    }

    private void disableShortlistButton(String buttonText) {
        mShortlistButton.setText(buttonText);

        // Change to checked icon
        int iconId = R.drawable.ic_course_taken;
        Drawable icon = getActivity().getApplicationContext().getResources().getDrawable(iconId);
        icon.setBounds(0, 0, 28, 28);  // TODO(david): Figure out a way to not hardcode this number
        mShortlistButton.setCompoundDrawables(icon, null, null, null);

        mShortlistButton.setEnabled(false);
    }

    private static class CoursePagerAdapter extends FragmentStatePagerAdapter {
        Fragment fragment1, fragment2, fragment3;
        private static final String[] TITLES = new String[]{
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
            switch (position) {
                case Constants.COURSE_SCHEDULE_PAGE_INDEX:
                    return fragment1;
//                    if (mCourseScheduleFragment == null) {
//                        courseScheduleFragment = new CourseScheduleFragment();
//                    };
//                    return courseScheduleFragment;
                case Constants.COURSE_ABOUT_PAGE_INDEX:
                    return fragment2;
                case Constants.COURSE_REVIEWS_PAGE_INDEX:
                    return fragment3;
                default:
                    return fragment2;
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

    protected class CourseUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (mCourseID != null)
                fetchCourseInfo(mCourseID);
        }
    }
}
