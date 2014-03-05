package com.uwflow.flow_android.fragment;

//import android.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.FriendListAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.*;
import com.uwflow.flow_android.entities.CourseFriend;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseAboutFragment extends Fragment {
    private static final String TAG = "CourseAboutFragment";

    private String mCourseID;

    private BaseAdapter mFriendListAdapter;
    private LinearLayout mFriendListContainer;
    private TextView mFriendListTextView;
    private ViewPager mParentViewPager;

    private TextView mDescTextView;
    private LinearLayout mRatingOverviewLayout;

    private CourseDetail mCourseDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	mCourseID = getArguments() != null ? getArguments().getString(Constants.COURSE_ID_KEY,
		Constants.COURSE_ID_DEFAULT) : Constants.COURSE_ID_DEFAULT;

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.course_about, container, false);

        mFriendListContainer = (LinearLayout)rootView.findViewById(R.id.friend_list);
	mFriendListTextView = (TextView)rootView.findViewById(R.id.friend_list_title);
        mParentViewPager = (ViewPager)getActivity().findViewById(R.id.pager);
	mDescTextView = (TextView)rootView.findViewById(R.id.course_desc);
	mRatingOverviewLayout = (LinearLayout) rootView.findViewById(R.id.rating_overview);

        // Configure "See individual reviews" button to point to the course reviews tab
        Button individualReviewsButton = (Button)rootView.findViewById(R.id.see_reviews_btn);
        individualReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mParentViewPager.setCurrentItem(Constants.COURSE_REVIEWS_PAGE_INDEX);
            }
        });

	return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
	super.onActivityCreated(savedInstanceState);

	fetchCourseFriends(mCourseID);
    }

    public void loadCourseInfo(CourseDetail courseDetail) {
	// TODO: null checking?
	mCourseDetail = courseDetail;

	mDescTextView.setText(mCourseDetail.getDescription());

	reloadRatingOverview();
    }

    private void reloadRatingOverview() {
	TextView overallRating = (TextView)mRatingOverviewLayout.findViewById(R.id.overall_rating);
	TextView overallCount = (TextView)mRatingOverviewLayout.findViewById(R.id.overall_count);
	ProgressBar usefulRating = (ProgressBar)mRatingOverviewLayout.findViewById(R.id.useful_bar);
	TextView usefulCount = (TextView)mRatingOverviewLayout.findViewById(R.id.useful_count);
	ProgressBar easyRating = (ProgressBar)mRatingOverviewLayout.findViewById(R.id.easy_bar);
	TextView easyCount = (TextView)mRatingOverviewLayout.findViewById(R.id.easy_count);

	overallRating.setText(String.format("%d%%", (int)(mCourseDetail.getOverall().getRating() * 100)));
	overallCount.setText(String.format("%d ratings", (int) mCourseDetail.getOverall().getCount()));
	usefulRating.setProgress((int) (Double.valueOf(mCourseDetail.getRatings().get(Rating.USEFULNESS).getRating()) * 100));
	usefulCount.setText(String.format("%d ratings", mCourseDetail.getRatings().get(Rating.USEFULNESS).getCount()));
	easyRating.setProgress((int) (Double.valueOf(mCourseDetail.getRatings().get(Rating.EASINESS).getRating()) * 100));
	easyCount.setText(String.format("%d ratings", mCourseDetail.getRatings().get(Rating.EASINESS).getCount()));
    }


    private void fetchCourseFriends(String course){
	FlowApiRequests.getCourseUsers(
		course,
		new FlowApiRequestCallbackAdapter() {
		    @Override
		    public void getCourseUsersCallback(CourseUserDetail courseUserDetail) {
			ArrayList<CourseFriend> courseFriends = getConsolidatedCourseFriendList(courseUserDetail);

			mFriendListAdapter = new FriendListAdapter(courseFriends, getActivity(), getActivity().getSupportFragmentManager());

			// Reload the TextView indicating the number of course friends
			mFriendListTextView.setText(String.format("%d friends took this", mFriendListAdapter.getCount()));

			// Re-populate UI with new data
			mFriendListContainer.removeAllViews();
			for (int i = 0; i < mFriendListAdapter.getCount(); i++) {
			    View item = mFriendListAdapter.getView(i, null, null);

			    mFriendListContainer.addView(item);
			}
                    }
		});
    }

    private ArrayList<CourseFriend> getConsolidatedCourseFriendList(CourseUserDetail courseUserDetail) {
	HashMap<String, User> friendMap = new HashMap<String, User>();

	// Insert all Users into a HashMap for fetching later
	for (User user : courseUserDetail.getUsers()) {
	    friendMap.put(user.getId(), user);
        }

	// Sort list of terms in reverse order (most recent first)
	ArrayList<TermUser> termUsers = courseUserDetail.getTermUsers();
	Collections.sort(termUsers, new CourseUserTermComparator());

	// Create a list of course friends based on the sorted order above
	ArrayList<CourseFriend> courseFriends = new ArrayList<CourseFriend>();
	for (TermUser termUser : termUsers) {
	    for (String userId : termUser.getUserIds()) {
		courseFriends.add(new CourseFriend(termUser.getTermName(), friendMap.get(userId)));
	    }
        }

	return courseFriends;
    }

    class CourseUserTermComparator implements Comparator<TermUser> {

	public CourseUserTermComparator() {}

	public int compare(TermUser a, TermUser b) {
	    if (a.getTermId().compareTo(b.getTermId()) > 0) {
		return -1;
	    } else {
		return 1;
	    }
	}
    }

}
