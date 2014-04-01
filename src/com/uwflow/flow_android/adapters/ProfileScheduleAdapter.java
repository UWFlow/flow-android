package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TableLayout;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.*;
import com.uwflow.flow_android.fragment.CourseFragment;
import com.uwflow.flow_android.util.CalendarHelper;
import com.uwflow.flow_android.util.CourseUtil;

import java.text.SimpleDateFormat;
import java.util.*;


public class ProfileScheduleAdapter extends BaseExpandableListAdapter {
    // This map holds all ScheduleCourse objects for the current week, keyed by the day of the week
    private Map<String,List<ScheduleCourse>> mScheduleMap;

    // This list holds the labels for all ExpandableListView groups
    private List<String> mWeekdayList;

    private Context mContext;
    private FragmentManager mFragmentManager;

    public ProfileScheduleAdapter(ScheduleCourses scheduleCourses, Context context) {
        mContext = context;
        mScheduleMap = filterAndCreateMap(scheduleCourses);
        mWeekdayList = new ArrayList<String>(mScheduleMap.keySet());
    }

    private Map<String, List<ScheduleCourse>> filterAndCreateMap(ScheduleCourses scheduleCourses) {
        // Map of ScheduleCourse objects to be passed into the adapter, bucketed by weekday
        Map<String, List<ScheduleCourse>> consolidatedMap = new LinkedHashMap<String, List<ScheduleCourse>>();

        // Sort list of ScheduleCourse objects
        List<ScheduleCourse> scheduleCourseList = scheduleCourses.getScheduleCourses();
        Collections.sort(scheduleCourseList, new ScheduleCourseComparator());

        // Filter out ScheduleCourse objects not in the current week
        // Add anything in the current week, bucketed by weekday (Monday-Sunday)

        Calendar entryCalendar = Calendar.getInstance();
        entryCalendar.setFirstDayOfWeek(Calendar.MONDAY);

        Calendar currentWeek = Calendar.getInstance();
        currentWeek.setFirstDayOfWeek(Calendar.MONDAY);
        currentWeek.set(Calendar.HOUR_OF_DAY, 0);
        currentWeek.clear(Calendar.MINUTE);
        currentWeek.clear(Calendar.SECOND);
        currentWeek.clear(Calendar.MILLISECOND);

        currentWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startOfWeek = currentWeek.getTime();

        currentWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date endOfWeek = currentWeek.getTime();

        for (ScheduleCourse scheduleCourse : scheduleCourseList) {
            Date entryDate = new Date(scheduleCourse.getStartDate());

            if (entryDate.before(startOfWeek)) {
                // Haven't reached the current week yet
                continue;
            } else if (entryDate.after(endOfWeek)) {
                // Past the current week
                return consolidatedMap;
            }

            entryCalendar.setTime(entryDate);
            String dayOfWeek = CalendarHelper.getDayOfWeek(entryCalendar.get(Calendar.DAY_OF_WEEK));

            if (!consolidatedMap.containsKey(dayOfWeek)) {
                consolidatedMap.put(dayOfWeek, new ArrayList<ScheduleCourse>());
            }

            (consolidatedMap.get(dayOfWeek)).add(scheduleCourse);
        }

        return consolidatedMap;
    }

    public int getCount() {
        int count = 0;
        for (List<ScheduleCourse> list: mScheduleMap.values()) {
            count += list.size();
        }
        return count;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return mScheduleMap.get(mWeekdayList.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ScheduleCourse course = (ScheduleCourse) getChild(groupPosition, childPosition);

        if (convertView == null || convertView.getId() == R.id.empty_shortlist_instructions) {
            // inflate a new view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile_schedule_row_item, parent, false);
        }

        TextView first = (TextView) convertView.findViewById(R.id.first);
        TextView second = (TextView) convertView.findViewById(R.id.second);

        first.setText(course.getCourseId());
        second.setText(String.valueOf(course.getStartDate()));

        String courseId = course.getCourseId();
        long startDateSeconds = course.getStartDate();
        long endDateSeconds = course.getEndDate();
        String locationBuilding = (course.getBuilding() != null) ? course.getBuilding() : "";
        String locationRoom = (course.getRoom() != null) ? course.getRoom() : "";
        String sectionType = (course.getSectionType() != null) ? course.getSectionType() : "";
        String sectionNumber = (course.getSectionNum() != null) ? course.getSectionNum() : "";

        if (courseId == null || startDateSeconds == 0 || endDateSeconds == 0) {
            // Don't bother printing out this item. Insufficient information.
            // We still want to insert a view so that our list and adapter items/indices remain in sync
            new View(mContext).setLayoutParams(new TableLayout.LayoutParams(0, 0));
        }

        final Date startDate = new Date(startDateSeconds);
        Date endDate = new Date(endDateSeconds);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mma");

        final String title = String.format("%s - %s %s",
                CourseUtil.humanizeCourseId(courseId),
                sectionType,
                sectionNumber);
        first.setText(title);
        final String location = String.format("%s %s", locationBuilding, locationRoom);
        second.setText(String.format("%s - %s    %s",
                timeFormat.format(startDate),
                timeFormat.format(endDate),
                location));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add an alarm for the current course
                mContext.startActivity(CalendarHelper.getAddAlarmIntent(title, location, startDate));
            }
        });

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return mScheduleMap.get(mWeekdayList.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return mWeekdayList.get(groupPosition);
    }

    public int getGroupCount() {
        return mWeekdayList.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile_courses_list_header_item, null);
        }
        TextView first = (TextView) convertView.findViewById(R.id.first);
        first.setTypeface(null, Typeface.BOLD);
        first.setText((String)getGroup(groupPosition));

        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ScheduleCourseComparator implements Comparator<ScheduleCourse> {

        public ScheduleCourseComparator() {}

        public int compare(ScheduleCourse a, ScheduleCourse b) {
            return a.getStartDate() < b.getStartDate() ? -1 :
                    a.getStartDate() > b.getStartDate() ? 1 : 0;
        }
    }
}
