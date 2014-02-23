package com.uwflow.flow_android.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.CourseReviewAdapter;
import com.uwflow.flow_android.entities.CourseReview;

import java.util.ArrayList;

/**
 * Created by jasperfung on 2/22/14.
 */
public class CourseReviewsFragment extends Fragment{
    private ListView mCourseReviewList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.course_reviews, container, false);
        mCourseReviewList = (ListView)rootView.findViewById(R.id.review_list);

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

        mCourseReviewList.setAdapter(new CourseReviewAdapter(courseReviewList, getActivity()));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }
}
