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

public class ProfileCoursesAdapter extends BaseAdapter {

    private List<Course> mCourseList;
    private Context mContext;

    public ProfileCoursesAdapter(List<Course> courses, Context context) {
        mCourseList = courses;
        mContext = context;
    }

    public int getCount() {
        return mCourseList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // verify that convertView is not null
        if (convertView == null) {
            // inflate a new view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile_courses_row_item, parent, false);
        }

        // Fill view with appropriate data
        TextView first, second;

        first = (TextView) convertView.findViewById(R.id.first);
        second = (TextView) convertView.findViewById(R.id.second);

        first.setText(mCourseList.get(position).getId());
        second.setText(mCourseList.get(position).getName());

        return convertView;
    }

    public Object getItem(int arg0) {
        return mCourseList.get(arg0);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
