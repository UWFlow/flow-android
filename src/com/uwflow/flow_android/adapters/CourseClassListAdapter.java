package com.uwflow.flow_android.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.Meeting;
import com.uwflow.flow_android.db_object.Section;
import com.uwflow.flow_android.util.StringHelper;

/**
 * Created by jasperfung on 2/22/14.
 */
public class CourseClassListAdapter extends BaseAdapter {
    private List<Section> mClasses;
    private Context mContext;

    public CourseClassListAdapter(List<Section> classes, Context context) {
        mClasses = classes;
        mContext = context;
    }

    public int getCount() {
        return mClasses.size();
    }

    public Object getItem(int arg0) {
        return mClasses.get(arg0);
    }

    public long getItemId(int position) {
        if (position < mClasses.size() && mClasses.get(position).getClassNum() != null) {
            return Long.valueOf(mClasses.get(position).getClassNum()).longValue();
        }
        return -1;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // verify that convertView is not null
        if (convertView == null) {
            // inflate a new view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.course_class_row_item, parent, false);
        }

        // Fill view with appropriate data
        TextView column1, column2;
        CheckBox checkbox;

        column1 = (TextView) convertView.findViewById(R.id.col1);
        column2 = (TextView) convertView.findViewById(R.id.col2);
        checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);

        Section currClass = mClasses.get(position);


        // Populate first column (class info)
        String sectionType = currClass.getSectionType();
        String sectionNumber = currClass.getSectionNum();
        Meeting meeting = currClass.getMeetings().get(0); // TODO: find out if we should ever handle >1 meeting
        String professor = meeting.getProfId();
        if (professor != null) {
            professor = StringHelper.capitalize(professor.replaceAll("_", " "));
        }
        int enrollmentTotal = currClass.getEnrollmentTotal();
        int enrollmentCapacity = currClass.getEnrollmentCapacity();

        // Make sure we don't have nulls
        if (sectionType == null) sectionType = "---";
        if (sectionNumber == null) sectionNumber = "---";
        if (professor == null) professor = "---";

        String string1 = String.format("%s %s\n%s\nSeats: %d/%d",
                sectionType,
                sectionNumber,
                professor,
                enrollmentTotal,
                enrollmentCapacity);
        column1.setText(string1);


        // Populate second column (time and location)
        String string2;
        String building = meeting.getBuilding();
        String room = meeting.getRoom();
        String campus = currClass.getCampus();

        String time;
        long startSeconds = meeting.getStartSeconds();
        long endSeconds = meeting.getEndSeconds();
        if (startSeconds == 0 || endSeconds == 0) {
            time = "N/A";
        } else {
            time = String.format("%s - %s",
                    getTimeFromSeconds(startSeconds),
                    getTimeFromSeconds(endSeconds));
        }
        if (campus == null) campus = "";
        if (building == null || room == null) {
            string2= String.format("%s<br>%s<br>%s",
                    time,
                    getFormattedDays(meeting.getDays()),
                    campus);
        } else {
            string2= String.format("%s<br>%s<br>%s %s - %s",
                    time,
                    getFormattedDays(meeting.getDays()),
                    building,
                    room,
                    campus);
        }
        column2.setText(Html.fromHtml(string2));


        // Enable notification subscription checkbox for at-capacity classes
        if (enrollmentTotal >= enrollmentCapacity) {
            // TODO: we need some way of checking the reminder state of this class
            checkbox.setChecked(true);
            checkbox.setVisibility(View.VISIBLE);
        } else {
            checkbox.setVisibility(View.INVISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox)v.findViewById(R.id.checkbox);
                cb.setChecked(!cb.isChecked());
                // TODO: subscribe for notifications of open seats in this class
            }
        });

        return convertView;
    }

    private String getTimeFromSeconds(long seconds) {
        int hours = (int)(seconds / 3600);
        int minutes = (int)(seconds % 3600) / 60;
        if (hours >= 12 && minutes >= 0) {
            if (hours > 12) hours -= 12;
            return String.format("%d:%dpm", hours, minutes);
        } else {
            return String.format("%d:%dam", hours, minutes);
        }
    }

    private String getFormattedDays(ArrayList<String> days) {
        if (days == null || days.size() == 0) {
            return "";
        }

        ArrayList<String> daySelection = new ArrayList<String>();
        daySelection.add("M");
        daySelection.add("T");
        daySelection.add("W");
        daySelection.add("Th");
        daySelection.add("F");
        daySelection.add("S");
        daySelection.add("Su");

        StringBuilder stringBuilder = new StringBuilder();
        for (String day : daySelection) {
            if (days.contains(day)) {
                // format the selected days
                stringBuilder.append("<b>");
                stringBuilder.append(day);
                stringBuilder.append("</b>");
            } else {
                stringBuilder.append("<font color=\"#C0C0C0\">");
                stringBuilder.append(day);
                stringBuilder.append("</font>");
            }
        }

        return stringBuilder.toString();
    }
}

