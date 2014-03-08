package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.Course;
import com.uwflow.flow_android.db_object.UserCourse;
import com.uwflow.flow_android.db_object.UserCourseDetail;
import com.uwflow.flow_android.fragment.CourseFragment;

import java.util.*;


public class ProfileCoursesAdapter extends BaseExpandableListAdapter {

    private Map<String,List<Course>> mConsolidatedMap;
    private List<String> mTermList;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public ProfileCoursesAdapter(UserCourseDetail userCourseDetail, Context context, FragmentManager fragmentManager) {
        mConsolidatedMap = createConsolidatedMap(userCourseDetail);
        mContext = context;
        mFragmentManager = fragmentManager;
        mTermList = new ArrayList<String>(mConsolidatedMap.keySet());
    }

    private Map<String, List<Course>> createConsolidatedMap(UserCourseDetail userCourseDetail) {
        // Map of Courses to be passed into the adapter, bucketed by term ID
        Map<String,List<Course>> consolidatedMap = new LinkedHashMap<String, List<Course>>();

        // Add all Courses (with names) to a HashMap for lookup
        HashMap<String, Course> courseMap = new HashMap<String, Course>();
        List<Course> courses = userCourseDetail.getCourses();
        for (Course course : courses) {
            courseMap.put(course.getId(), course);
        }

        // Sort list of UserCourses (without course names) by term number
        List<UserCourse> userCourseList = userCourseDetail.getUserCourses();
        Collections.sort(userCourseList, new UserCourseTermComparator());

        // Iterate through sorted list of UserCourses and add the respective Courses (with course names)
        // to the consolidatedMap, bucketed by term ID
        for (UserCourse userCourse : userCourseList) {
            String termName = userCourse.getTermName();
            if (!consolidatedMap.containsKey(termName)) {
                consolidatedMap.put(termName, new ArrayList<Course>());
            }

            (consolidatedMap.get(termName)).add(courseMap.get(userCourse.getCourseId()));
        }
        return consolidatedMap;
    }

    public int getCount() {
        int count = 0;
        for (String term : mTermList) {
            count += mConsolidatedMap.get(term).size();
        }
        return count;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mConsolidatedMap.get(mTermList.get(groupPosition)).get(childPosition);
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

        first.setText(course.getCode());
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
        return mConsolidatedMap.get(mTermList.get(groupPosition)).size();
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

    class UserCourseTermComparator implements Comparator<UserCourse> {

        public UserCourseTermComparator() {}

        public int compare(UserCourse a, UserCourse b) {
            if (a.getTermId().compareTo(b.getTermId()) > 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
