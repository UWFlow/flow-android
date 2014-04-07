package com.uwflow.flow_android.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.Meeting;
import com.uwflow.flow_android.db_object.Section;
import com.uwflow.flow_android.network.FlowApiRequestCallback;
import com.uwflow.flow_android.network.FlowApiRequests;
import com.uwflow.flow_android.util.CourseUtil;
import com.uwflow.flow_android.util.RegistrationIdUtil;
import com.uwflow.flow_android.util.StringHelper;
import com.uwflow.flow_android.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jasperfung on 2/22/14.
 */
public class CourseClassListAdapter extends BaseAdapter {
    private static final String TAG = CourseClassListAdapter.class.getSimpleName();

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
        TextView column1, column2, seatsAvailableTextView;

        column1 = (TextView) convertView.findViewById(R.id.col1);
        column2 = (TextView) convertView.findViewById(R.id.col2);
        seatsAvailableTextView = (TextView) convertView.findViewById(R.id.seats_available_textview);
        final ImageButton addAlertButton = (ImageButton) convertView.findViewById(R.id.add_alert_button);

        final Section currClass = mClasses.get(position);

        // Populate first column (class info)
        String sectionType = currClass.getSectionType();
        String sectionNumber = currClass.getSectionNum();
        Meeting meeting = currClass.getMeetings().get(0); // TODO: Handle more than 1 meeting

        // Make sure we don't have nulls
        if (sectionType == null) sectionType = "";
        if (sectionNumber == null) sectionNumber = "";

        column1.setText(String.format("%s\n%s", sectionType, sectionNumber));

        // Populate second column (class details)
        List<String> detailsList = new LinkedList<String>();
        String building = meeting.getBuilding();
        String room = meeting.getRoom();
        String campus = currClass.getCampus();
        if (campus == null) campus = "";
        String professor = meeting.getProfId();

        long startSeconds = meeting.getStartSeconds();
        long endSeconds = meeting.getEndSeconds();
        if (startSeconds != 0 && endSeconds != 0) {
            detailsList.add(String.format("%s - %s",
                    getTimeFromSeconds(startSeconds),
                    getTimeFromSeconds(endSeconds)));
        }
        if (meeting.getDays() != null && !meeting.getDays().isEmpty()) {
            detailsList.add(getFormattedDays(meeting.getDays()));
        }
        if (StringUtils.isNotEmpty(building) || StringUtils.isNotEmpty(room)) {
            detailsList.add(String.format("%s %s - %s", building, room, campus));
        } else if (StringUtils.isNotEmpty(campus)) {
            detailsList.add(campus);
        }
        if (StringUtils.isNotEmpty(professor)) {
            professor = StringHelper.capitalize(professor.replaceAll("_", " "));
            detailsList.add(professor);
        }
        column2.setText(Html.fromHtml(StringUtils.join(detailsList, "<br>")));

        // Populate seats available column
        int enrollmentTotal = currClass.getEnrollmentTotal();
        int enrollmentCapacity = currClass.getEnrollmentCapacity();
        String fractionHtml = String.format("%s/%s<br>seats", enrollmentTotal, enrollmentCapacity);
        seatsAvailableTextView.setText(Html.fromHtml(fractionHtml));

        // Enable notification subscription button for at-capacity classes
        // TODO(david): Would be good to change button styling if alert already added
        // TODO(david): Change back to checkbox (and style it properly) if above is done.
        final String registrationId = RegistrationIdUtil.getRegistrationId(mContext);
        if (RegistrationIdUtil.supportsGcm(mContext) && StringUtils.isNotEmpty(registrationId) &&
                enrollmentTotal >= enrollmentCapacity) {
            addAlertButton.setVisibility(View.VISIBLE);
        } else {
            addAlertButton.setVisibility(View.INVISIBLE);
        }

        final String finalSectionType = sectionType;
        final String finalSectionNumber = sectionNumber;
        final String userId = UserUtil.getLoggedInUserId(mContext);

        addAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String courseId = currClass.getCourseId();
                final String humanizedCourseId = CourseUtil.humanizeCourseId(courseId);
                final String termId = currClass.getTermId();
                String registrationId = RegistrationIdUtil.getRegistrationId(mContext);

                addAlertButton.setEnabled(false);

                FlowApiRequests.addCourseAlert(registrationId, courseId, termId, finalSectionType, finalSectionNumber,
                        userId, new FlowApiRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        String message = String.format(
                                "You will be notified when a seat opens up for %s: %s %s.",
                                humanizedCourseId, finalSectionType, finalSectionNumber);
                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(String error) {
                        String message;

                        // TODO(david): Server should return error code instead
                        if (StringUtils.containsIgnoreCase(error, "already exists")) {
                            message = String.format("You've already added an alert for %s: %s %s",
                                    humanizedCourseId, finalSectionType, finalSectionNumber);
                        } else {
                            message = String.format("Uh oh, could not add a course alert for %s. Error: %s",
                                    humanizedCourseId, error);
                            Log.d(TAG, message);
                            Crashlytics.log(Log.ERROR, TAG, message);
                        }

                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                        addAlertButton.setEnabled(true);
                    }
                });
            }
        });

        return convertView;
    }

    private String getTimeFromSeconds(long seconds) {
        int hours = (int)(seconds / 3600);
        int minutes = (int)(seconds % 3600) / 60;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);

        return new SimpleDateFormat("h:mma").format(cal.getTime());
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

