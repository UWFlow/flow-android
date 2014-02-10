package com.uwflow.flow_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import com.uwflow.flow_android.services.AlarmClockService;
import android.util.Log;


public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        AlarmClockService alarmService = new AlarmClockService();
        startActivity(alarmService.setAlarmForTimeWithMessage("test",5,55,2));

        /*
        Intent alarmClockSetIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
        alarmClockSetIntent.putExtra(AlarmClock.EXTRA_MESSAGE, "test");
        alarmClockSetIntent.putExtra(AlarmClock.EXTRA_HOUR, 15);
        alarmClockSetIntent.putExtra(AlarmClock.EXTRA_MINUTES, 37);
        startActivity(alarmClockSetIntent);
        */

    }
}
