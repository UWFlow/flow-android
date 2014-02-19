package com.uwflow.flow_android;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.uwflow.flow_android.fragment.AboutFragment;
import com.uwflow.flow_android.fragment.ExploreFragment;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.fragment.ShortlistFragment;

import java.util.ArrayList;

public class MainFlowActivity extends FlowActivity {
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private String [] LIST_VALUE = {"Profile", "Explore", "Shortlist", "About"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_main);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerList.setItemChecked(0, true);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }


    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;
        switch (position){
            case(0) : fragment = new ProfileFragment();break;
            case(1) : fragment = new ExploreFragment();break;
            case(2) : fragment = new ShortlistFragment();break;
            case(3) : fragment = new AboutFragment();break;
        }

        if (fragment != null){
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }

        // Insert the fragment by replacing any existing fragment


        // Highlight the selected item, update the title, and close the drawer
        drawerList.setItemChecked(position, true);
        //setTitle(mPlanetTitles[position]);
        drawerLayout.closeDrawer(drawerList);
    }
}
