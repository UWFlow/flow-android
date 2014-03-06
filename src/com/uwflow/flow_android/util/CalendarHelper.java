package com.uwflow.flow_android.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import com.uwflow.flow_android.db_object.Exam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jasperfung on 3/2/14.
 */
public class CalendarHelper {
    private static final String seasons[] = {
            "Winter", "Winter", "Winter", "Winter", "Spring", "Spring",
            "Spring", "Spring", "Fall", "Fall", "Fall", "Fall"
    };
    private static final String shortMonth[] = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    public static String getSeason( Date date ) {
        return seasons[ date.getMonth() ];
    }

    public static String getShortMonth( Date date ) {
        return shortMonth[ date.getMonth() ];
    }

    public static String getShortString(Date date) {
        StringBuilder sb = new StringBuilder();
        DateFormat df = new SimpleDateFormat("yy");
        sb.append(getShortMonth(date));
        sb.append(" ");
        sb.append(df.format(date));
        return sb.toString();
    }

    public static String getDateString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat ("E, MMM d");
        return df.format(date);
    }

    public static String getTimeString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat ("h:mma");
        return df.format(date);
    }

    public static String formatTermNicely(String termId) {
        if (termId == null) return null;

        String[] tokens = termId.split("_");

        int year = Integer.valueOf(tokens[0]).intValue();
        int month = Integer.valueOf(tokens[1]).intValue() - 1;

        if (month >= 12) return null;

        return String.format("%s %d", seasons[month], year);
    }

    public static Intent getAddCalenderEventIntent(Exam exam) {
        String courseId = exam.getCourseId();
        String sections = exam.getSections();
        String location = exam.getLocation();
        String startDate = "";
        String endDate = "";
        String startTime = "";
        String endTime = "";

        Intent intent = new Intent(Intent.ACTION_INSERT);
        if (exam.getStartDate() != 0 && exam.getEndDate() != 0) {
            // Missing start/end date. Let's leave this to the user to enter
            startDate = getDateString(new Date(exam.getStartDate()));
            endDate = getDateString(new Date(exam.getEndDate()));
            startTime = getTimeString(new Date(exam.getStartDate()));
            endTime = getTimeString(new Date(exam.getEndDate()));

            // TODO: verify that the format of exam.getStartDate() is appropriate
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, exam.getStartDate())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, exam.getEndDate());

        }
        intent.setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE,
                        String.format("%s Exam", courseId.toUpperCase()))
                .putExtra(CalendarContract.Events.DESCRIPTION,
                        String.format("Course: %s\nSection: %s\nDate: %s\nTime: %s - %s\nLocation: %s",
                                courseId,
                                sections,
                                startDate,
                                startTime,
                                endTime,
                                location))
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
        return intent;
    }
}
