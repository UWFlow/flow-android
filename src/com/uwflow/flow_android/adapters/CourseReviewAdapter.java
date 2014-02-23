package com.uwflow.flow_android.adapters;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.entities.CourseReview;

/**
 * Created by jasperfung on 2/22/14.
 */
public class CourseReviewAdapter extends BaseAdapter {
    private ArrayList<CourseReview> mReviews;
    private Context mContext;

    public CourseReviewAdapter(ArrayList<CourseReview> reviews, Context context) {
        mReviews = reviews;
        mContext = context;
    }

    public int getCount() {
        return mReviews.size();
    }

    public Object getItem(int arg0) {
        return mReviews.get(arg0);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // verify that convertView is not null
        if (convertView == null) {
            // inflate a new view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.course_review_row_item, parent, false);
        }

        // Fill view with appropriate data
        TextView first, second, reviewText;
        ImageView image;
        CheckBox status1, status2, status3;

        first = (TextView) convertView.findViewById(R.id.first);
        second = (TextView) convertView.findViewById(R.id.second);
        reviewText = (TextView) convertView.findViewById(R.id.review_text);

        image = (ImageView) convertView.findViewById(R.id.image);

        status1 = (CheckBox) convertView.findViewById(R.id.status1);
        status2 = (CheckBox) convertView.findViewById(R.id.status2);
        status3 = (CheckBox) convertView.findViewById(R.id.status3);

        first.setText(mReviews.get(position).getName());
        second.setText(mReviews.get(position).getDate());
        reviewText.setText(mReviews.get(position).getReview());

        status1.setChecked(mReviews.get(position).isUseful());
        status2.setChecked(mReviews.get(position).isEasy());
        status3.setChecked(mReviews.get(position).isLikedIt());


        if (mReviews.get(position).getImage() == null) {
            image.setImageResource(R.drawable.photo_profile_empty);
        } else {
            image.setImageBitmap(mReviews.get(position).getImage());
        }

        return convertView;
    }
}

