package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.Course;

import java.util.List;

/**
 * Created by jasperfung on 3/4/14.
 */
public class SearchResultAdapter extends BaseAdapter {
    private List<Course> mResults;
    private Context mContext;

    public SearchResultAdapter(List<Course> results, Context context) {
        mResults = results;
        mContext = context;
    }

    public int getCount() {
        return mResults.size();
    }

    public Object getItem(int arg0) {
        return mResults.get(arg0);
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
            convertView = inflater.inflate(R.layout.search_row_item, parent, false);
        }

        // Fill view with appropriate data
        TextView first, second, third, fourth;

        first = (TextView) convertView.findViewById(R.id.course_code);
        second = (TextView) convertView.findViewById(R.id.course_name);
        third = (TextView) convertView.findViewById(R.id.rating);
        fourth = (TextView) convertView.findViewById(R.id.rating_count);

        final Course course = mResults.get(position);

        first.setText(course.getCode());
        second.setText(course.getName());
        Double rating = course.getOverall().getRating();
        if (rating == null) {
            third.setText("---");
        } else {
            third.setText(String.format("%d%%", (int)(rating * 100)));
        }
        fourth.setText(String.format("%d ratings", (int)(course.getOverall().getCount())));

        return convertView;
    }
}
