package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.CourseReviewAdapter;
import com.uwflow.flow_android.entities.CourseReview;

import java.util.ArrayList;

/**
 * Created by jasperfung on 2/22/14.
 */
public class CourseReviewsFragment extends Fragment{
    private LinearLayout mCourseReviewListContainer;
    private TextView mEmptyReviewListContainerView;

    private BaseAdapter mCourseReviewListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.course_reviews, container, false);
        mCourseReviewListContainer = (LinearLayout)rootView.findViewById(R.id.review_list);
        mEmptyReviewListContainerView = (TextView)rootView.findViewById(R.id.empty_review_list);


        // TODO: replace this arraylist with whatever real data source
        ArrayList<CourseReview> courseReviewList = new ArrayList<CourseReview>();
        for (int i = 0; i < 5; i++) {
            String name = "Username" + i;
            String date = "Sept " + (2014 + i);
            String review = "I loved this course. You don't just learn about music, you also learn about American history, politics, psychology, etc. This isn't just a plain old music class, but a course that ties together a number of subjects into one big real picture.";
            boolean useful = (i % 2 == 0) ? true : false;
            boolean easy = !useful;
            boolean likedIt = useful;
            Log.e("BUGGER", "printout #" + i);

            courseReviewList.add(new CourseReview(name, date, review, null, useful, easy, likedIt));
        }

        mCourseReviewListAdapter = new CourseReviewAdapter(courseReviewList, getActivity());

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

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }
}
