package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.Course;
import com.uwflow.flow_android.fragment.CourseFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProfileCoursesAdapter extends BaseExpandableListAdapter {

    private List<Course> mCourseList;
    private List<String> mTermList;
    Map<String,List<Course>> mCoursesCollection;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public ProfileCoursesAdapter(List<Course> courses, Context context, FragmentManager fragmentManager) {
        mCourseList = courses;
        mContext = context;
        mFragmentManager = fragmentManager;
        createTermsList();
        createCollection();
    }

    public void createTermsList() {
        mTermList = new ArrayList<String>();
        mTermList.add("Winter 2014");
        mTermList.add("Fall 2013");
        mTermList.add("Spring 2013");
        mTermList.add("Winter 2013");
    }

    public void createCollection() {

        mCoursesCollection = new LinkedHashMap<String, List<Course>>();



        //TODO:Once Course has term field, add correct course to the right term
        for(String term : mTermList) {
            List<Course> childList = new ArrayList<Course>();
            if (term.equals("Winter 2014")) {
                for(int i = 0; i<mCourseList.size()/4; i++) {
                    childList.add(mCourseList.get(i));
                }
            }
            if (term.equals("Fall 2013")) {
                for(int i = mCourseList.size()/4; i<mCourseList.size()/2; i++) {
                    childList.add(mCourseList.get(i));
                }
            }
            if (term.equals("Spring 2013")) {
                for(int i = mCourseList.size()/2; i<3*mCourseList.size()/4; i++) {
                    childList.add(mCourseList.get(i));
                }
            }
            if (term.equals("Winter 2013")) {
                for(int i = 3*mCourseList.size()/4; i<mCourseList.size(); i++) {
                    childList.add(mCourseList.get(i));
                }
            }
            mCoursesCollection.put(term,childList);
        }
    }

    public int getCount() {
        return mCourseList.size();
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mCoursesCollection.get(mTermList.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Course course = (Course) getChild(groupPosition, childPosition);

        if (convertView == null) {
            // inflate a new view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile_courses_row_item, parent, false);
        }

        TextView first = (TextView) convertView.findViewById(R.id.first);
        TextView second = (TextView) convertView.findViewById(R.id.second);

        first.setText(course.getId());
        second.setText(course.getName());

        // Make this View clickable to open a new CourseFragment
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.replace(R.id.content_frame, CourseFragment.newInstance(course.getId()))
                        .addToBackStack(null)
                        .commit();
            }
        };
        convertView.setOnClickListener(onClickListener);

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return mCoursesCollection.get(mTermList.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return mTermList.get(groupPosition);
    }

    public int getGroupCount() {
        return mTermList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

/*
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
*/

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String termName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile_courses_list_header_item, null);
        }
        TextView first = (TextView) convertView.findViewById(R.id.first);
        first.setTypeface(null, Typeface.BOLD);
        first.setText(termName);

        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public Object getItem(int arg0) {
        return mCourseList.get(arg0);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
