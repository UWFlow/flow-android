package com.uwflow.flow_android.adapters;

import java.util.ArrayList;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.entities.CourseClass;

/**
 * Created by jasperfung on 2/22/14.
 */
public class CourseClassListAdapter extends BaseAdapter {
    private ArrayList<CourseClass> mClasses;
    private Context mContext;

    public CourseClassListAdapter(ArrayList<CourseClass> reviews, Context context) {
        mClasses = reviews;
        mContext = context;
    }

    public int getCount() {
        return mClasses.size();
    }

    public Object getItem(int arg0) {
        return mClasses.get(arg0);
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
            convertView = inflater.inflate(R.layout.course_class_row_item, parent, false);
        }

        // Fill view with appropriate data
        TextView column1, column2;
        CheckBox checkbox;

        column1 = (TextView) convertView.findViewById(R.id.col1);
        column2 = (TextView) convertView.findViewById(R.id.col2);
        checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);

        CourseClass currClass = mClasses.get(position);

        String string1 = String.format("%s %03d\n%s\nSeats: %d/%d",
                currClass.getSectionType(),
                currClass.getSectionNum(),
                currClass.getProfessor(),
                currClass.getEnrollmentTotal(),
                currClass.getEnrollmentCapacity());
        column1.setText(string1);

        String string2 = String.format("%s - %s<br>%s<br>%s %s - %s",
                getTimeFromSeconds(currClass.getStartTimeSeconds()),
                getTimeFromSeconds(currClass.getEndTimeSeconds()),
                getFormattedDays(currClass.getDays()),
                currClass.getBuilding(),
                currClass.getRoom(),
                currClass.getCampus());
        column2.setText(Html.fromHtml(string2));

        if (currClass.getEnrollmentTotal() >= currClass.getEnrollmentCapacity()) {
            // TODO: we need some way of checking the reminder state of this class
            checkbox.setChecked(currClass.getSubscribed());
        } else {
            checkbox.setVisibility(View.INVISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox)v.findViewById(R.id.checkbox);
                cb.setChecked(!cb.isChecked());
            }
        });

        return convertView;
    }

    private String getTimeFromSeconds(Integer seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        if (hours >= 12 && minutes >= 0) {
            if (hours > 12) hours -= 12;
            return String.format("%d:%dpm", hours, minutes);
        } else {
            return String.format("%d:%dam", hours, minutes);
        }
    }

    private String getFormattedDays(ArrayList<String> days) {
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

//        for (String day : days) {
//            stringBuilder.append(day);
//        }

        return stringBuilder.toString();
    }
}

