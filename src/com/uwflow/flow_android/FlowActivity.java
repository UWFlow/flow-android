package com.uwflow.flow_android;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;

public class FlowActivity extends FragmentActivity {
    protected FlowDatabaseHelper flowDatabaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.flow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (flowDatabaseHelper != null) {
            OpenHelperManager.releaseHelper();
            flowDatabaseHelper = null;
        }
    }

    public FlowDatabaseHelper getHelper() {
        if (flowDatabaseHelper == null) {
            flowDatabaseHelper =
                    OpenHelperManager.getHelper(this, FlowDatabaseHelper.class);
        }
        return flowDatabaseHelper;
    }
}
