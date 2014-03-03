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
import com.uwflow.flow_android.entities.CourseInfo;
import com.uwflow.flow_android.entities.Friend;

import java.util.ArrayList;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseAboutFragment extends Fragment {
    private BaseAdapter mFriendListAdapter;
    private LinearLayout mFriendListContainer;
    private ViewPager mParentViewPager;

    private TextView mDescTextView;
    private LinearLayout mRatingOverviewLayout;

    private CourseInfo mCourseInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.course_about, container, false);

        mFriendListContainer = (LinearLayout)rootView.findViewById(R.id.friend_list);
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

        // TODO: replace this arraylist with whatever real data source
        ArrayList<Friend> friendList = new ArrayList<Friend>();
        for (int i = 0; i < 5; i++) {
            String first = "Username " + i;
            String second = "Sept " + (2014 + i);
            friendList.add(new Friend(first, second, null, i + 100));
        }

        mFriendListAdapter = new FriendListAdapter(friendList, getActivity());

        // Generate LinearLayouts for every list item
        for (int i = 0; i < mFriendListAdapter.getCount(); i++) {
            View item = mFriendListAdapter.getView(i, null, null);
            mFriendListContainer.addView(item);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    public void loadCourseInfo(CourseInfo courseInfo) {
        // TODO: null checking?
        mCourseInfo = courseInfo;

        mDescTextView.setText(mCourseInfo.getDescription());

        reloadRatingOverview();
    }

    private void reloadRatingOverview() {
        TextView overallRating = (TextView)mRatingOverviewLayout.findViewById(R.id.overall_rating);
        TextView overallCount = (TextView)mRatingOverviewLayout.findViewById(R.id.overall_count);
        ProgressBar usefulRating = (ProgressBar)mRatingOverviewLayout.findViewById(R.id.useful_bar);
        TextView usefulCount = (TextView)mRatingOverviewLayout.findViewById(R.id.useful_count);
        ProgressBar easyRating = (ProgressBar)mRatingOverviewLayout.findViewById(R.id.easy_bar);
        TextView easyCount = (TextView)mRatingOverviewLayout.findViewById(R.id.easy_count);

        overallRating.setText(String.format("%d%%", (int)(mCourseInfo.getOverallRating().getOverallRating() * 100)));
        overallCount.setText(String.format("%d ratings", (int)mCourseInfo.getOverallRating().getOverallCount()));
        usefulRating.setProgress((int)(mCourseInfo.getOverallRating().getUsefulRating() * 100));
        usefulCount.setText(String.format("%d ratings", mCourseInfo.getOverallRating().getUsefulCount()));
        easyRating.setProgress((int)(mCourseInfo.getOverallRating().getEasyRating() * 100));
        easyCount.setText(String.format("%d ratings", mCourseInfo.getOverallRating().getEasyCount()));
    }
}
