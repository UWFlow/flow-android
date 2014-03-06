package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.Course;
import com.uwflow.flow_android.fragment.CourseFragment;

import java.util.List;

public class ProfileCoursesAdapter extends BaseAdapter {
    private List<Course> mCourseList;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public ProfileCoursesAdapter(List<Course> courses, Context context, FragmentManager fragmentManager) {
        mCourseList = courses;
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    public int getCount() {
        return mCourseList.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
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

        // Make this View clickable to open a new CourseFragment
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, CourseFragment.newInstance(mCourseList.get(position).getId()))
                        .addToBackStack(null)
                        .commit();
            }
        };
        convertView.setOnClickListener(onClickListener);

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
