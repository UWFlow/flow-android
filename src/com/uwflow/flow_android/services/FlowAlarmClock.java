package com.uwflow.flow_android.services;

import android.app.IntentService;
import android.content.Intent;
import android.provider.AlarmClock;

/**
 * Created by Chinmay on 2/10/14.
 */
public class FlowAlarmClock {

    public Intent setAlarmForTimeWithMessage(String message,int hour, int minutes, int timeBefore) {
            timeBefore = Math.abs(timeBefore);
            if (minutes-timeBefore < 0) {
                minutes = (minutes-timeBefore)%60;
                hour -= Math.floor((minutes-timeBefore)/60);
            } else {
                minutes -= timeBefore;
            }
            Intent alarmClockIntent = new Intent(android.provider.AlarmClock.ACTION_SET_ALARM);
            alarmClockIntent.putExtra(android.provider.AlarmClock.EXTRA_MESSAGE, message);
            alarmClockIntent.putExtra(android.provider.AlarmClock.EXTRA_HOUR, hour);
            alarmClockIntent.putExtra(android.provider.AlarmClock.EXTRA_MINUTES, minutes);
            return alarmClockIntent;
    }

}
