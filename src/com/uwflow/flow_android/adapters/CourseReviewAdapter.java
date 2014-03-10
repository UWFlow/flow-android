package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.Rating;
import com.uwflow.flow_android.db_object.Review;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.util.CalendarHelper;

import java.util.Date;
import java.util.List;

/**
 * Created by jasperfung on 2/22/14.
 */
public class CourseReviewAdapter extends BaseAdapter {
    private List<Review> mReviews;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public CourseReviewAdapter(List<Review> reviews, Context context, FragmentManager fragmentManager) {
        mReviews = reviews;
        mContext = context;
        mFragmentManager = fragmentManager;
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

        final Review review = mReviews.get(position);

        String programName = review.getAuthor().getProgramName();
        if (programName != null) {
            // Set reviewer name
            first.setText(String.format("A %s student", programName));

            // Use placeholder image
            image.setImageResource(R.drawable.kitty);

            // Remove any OnClickListeners that may be attached to this review
            image.setOnClickListener(null);
            first.setOnClickListener(null);
        } else {
            // Set reviewer name
            first.setText(review.getAuthor().getName());

            // Fetch facebook image
            Picasso.with(mContext).load(review.getAuthor().getProfilePicUrl()).placeholder(R.drawable.kitty).into(image);

            // Make this View clickable to go to view that user's profile in our app
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragmentManager.beginTransaction()
                            .replace(R.id.content_frame, ProfileFragment.newInstance(review.getAuthor().getId()))
                            .addToBackStack(null)
                            .commit();
                }
            };
            image.setOnClickListener(onClickListener);
            first.setOnClickListener(onClickListener);
        }

        // Set date field
        second.setText(CalendarHelper.getShortString(new Date(review.getCommentDate())));
        reviewText.setText(review.getComment());

        // Set state of ratings
        String usefulRating = review.getRatings().get(Rating.USEFULNESS).getRating();
        String easyRating = review.getRatings().get(Rating.EASINESS).getRating();
        String likedItRating = review.getRatings().get(Rating.INTEREST).getRating();

        if (usefulRating == null) {
            status1.setEnabled(true);
        } else {
            status1.setEnabled(false);
            status1.setChecked(Double.valueOf(usefulRating) == 1);
        }
        if (easyRating== null) {
            status2.setEnabled(true);
        } else {
            status2.setEnabled(false);
            status2.setChecked(Double.valueOf(easyRating) == 1);
        }
        if (likedItRating == null) {
            status3.setEnabled(true);
        } else {
            status3.setEnabled(false);
            status3.setChecked(Double.valueOf(likedItRating) == 1);
        }

        return convertView;
    }
}

