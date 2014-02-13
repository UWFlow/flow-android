package com.uwflow.flow_android;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import com.uwflow.flow_android.services.FlowAlarmClock;
import com.uwflow.flow_android.services.FlowCalendarEvent;

import java.util.Calendar;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }

    public void sampleCalendarEvent() {
        FlowCalendarEvent calendarService = new FlowCalendarEvent();
        calendarService.addCalenderEvent(getContentResolver());
    }

    public void sampleAlarmEvent() {
        FlowAlarmClock alarmService = new FlowAlarmClock();
        startActivity(alarmService.setAlarmForTimeWithMessage("test",5,55,2));
    }

}
