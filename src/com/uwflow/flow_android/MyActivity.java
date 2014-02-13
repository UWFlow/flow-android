package com.uwflow.flow_android;

import android.app.Activity;
import android.os.Bundle;
import com.uwflow.flow_android.services.AlarmClockService;


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

    }
}
