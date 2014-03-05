package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.Exam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProfileExamAdapter extends BaseAdapter {

    private List<Exam> mExamList;
    private Context mContext;

    public ProfileExamAdapter(List<Exam> exams, Context context) {
        mExamList = exams;
        mContext = context;
    }

    public int getCount() {
        return mExamList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // verify that convertView is not null
        if (convertView == null) {
            // inflate a new view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile_exams_row_item, parent, false);
        }

        // Fill view with appropriate data
        TextView first, second;

        first = (TextView) convertView.findViewById(R.id.first);
        second = (TextView) convertView.findViewById(R.id.second);

        Exam exam = mExamList.get(position);

        String examID = exam.getCourseId();
        long startDateSeconds = exam.getStartDate();
        long endDateSeconds = exam.getEndDate();
        String location = exam.getLocation();
        String sections = exam.getSections();

        if (examID == null || startDateSeconds == 0 || endDateSeconds == 0) {
            // Don't bother printing out this exam. Insufficient information
            new View(mContext).setLayoutParams(new TableLayout.LayoutParams(0, 0));
        }

        Date startDate = new Date(startDateSeconds);
        Date endDate = new Date(endDateSeconds);
        SimpleDateFormat startDateFormat = new SimpleDateFormat ("E, MMM d    h:mma");
        SimpleDateFormat endDateFormat = new SimpleDateFormat ("hh:mma");

        first.setText(examID.toUpperCase());
	/* TODO: fetch the course name and place it in a third TextView */
        second.setText(String.format("%s - %s    %s",
                startDateFormat.format(startDate),
                endDateFormat.format(endDate),
                location));


        return convertView;
    }

    public Object getItem(int arg0) {
        return mExamList.get(arg0);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}
