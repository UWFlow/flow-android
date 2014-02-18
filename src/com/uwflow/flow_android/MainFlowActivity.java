package com.uwflow.flow_android;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.uwflow.flow_android.fragment.ProfileFragment;

import java.util.ArrayList;

/**
 * Created by wentaoji on 2014-02-17.
 */
public class MainFlowActivity extends FlowActivity {
    private ListView drawerList;
    private String [] LIST_VALUE = {"Profile", "Explore", "Shortlist", "About"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_main);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayList<String> listValue = new ArrayList<String>();
        for (String s : LIST_VALUE){
            listValue.add(s);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listValue);
        drawerList.setAdapter(adapter);

        Fragment profileFrag = new ProfileFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content_frame, profileFrag)
                .commit();
    }
}
