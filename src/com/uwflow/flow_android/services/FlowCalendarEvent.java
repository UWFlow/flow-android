package com.uwflow.flow_android.services;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;

/**
 * Created by Chinmay on 2/12/14.
 */
public class FlowCalendarEvent {

    public long addCalenderEvent(ContentResolver crPassed) {

        long calID = 1;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2014, 2, 13, 7, 30);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2014, 2, 13, 8, 45);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = crPassed;
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, "TempTitle");
        values.put(CalendarContract.Events.DESCRIPTION, "Workout");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        //values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        long eventID = Long.parseLong(uri.getLastPathSegment());
        return eventID;
    }

}
