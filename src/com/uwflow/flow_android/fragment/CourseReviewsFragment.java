package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.CourseReviewAdapter;
import com.uwflow.flow_android.db_object.CourseDetail;
import com.uwflow.flow_android.db_object.Rating;

/**
 * Created by jasperfung on 2/22/14.
 */
public class CourseReviewsFragment extends Fragment{
    private LinearLayout mCourseReviewListContainer;
    private TextView mEmptyReviewListContainerView;
    private LinearLayout mRatingOverviewLayout;

    private BaseAdapter mCourseReviewListAdapter;

    private CourseDetail mCourseDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.course_reviews, container, false);
        mCourseReviewListContainer = (LinearLayout)rootView.findViewById(R.id.review_list);
        mEmptyReviewListContainerView = (TextView)rootView.findViewById(R.id.empty_review_list);
        mRatingOverviewLayout = (LinearLayout) rootView.findViewById(R.id.rating_overview);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    public void loadCourseInfo(CourseDetail courseDetail) {
        // TODO: null checking?
        mCourseDetail = courseDetail;

        reloadRatingOverview();

        reloadReviews();
    }

    private void reloadReviews() {
        mCourseReviewListAdapter = new CourseReviewAdapter(mCourseDetail.getReviews(), getActivity());
        // clear existing content
        // TODO: we need a smarter way of doing this so that we don't redundantly redraw review objects
        mCourseReviewListContainer.removeAllViews();

        if (mCourseReviewListAdapter.getCount() > 0) {
            mEmptyReviewListContainerView.setVisibility(View.GONE);
            mCourseReviewListContainer.setVisibility(View.VISIBLE);

            // Generate LinearLayouts for every list item
            for (int i = 0; i < mCourseReviewListAdapter.getCount(); i++) {
                View item = mCourseReviewListAdapter.getView(i, null, null);
                mCourseReviewListContainer.addView(item);
            }

        } else {
            mEmptyReviewListContainerView.setVisibility(View.VISIBLE);
            mCourseReviewListContainer.setVisibility(View.GONE);
        }
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
}
