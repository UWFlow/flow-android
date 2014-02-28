package com.uwflow.flow_android;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.uwflow.flow_android.adapters.NavDrawerAdapter;
import com.uwflow.flow_android.entities.NavDrawerItem;
import com.uwflow.flow_android.fragment.AboutFragment;
import com.uwflow.flow_android.fragment.ExploreFragment;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.fragment.ShortlistFragment;

import java.util.ArrayList;

public class MainFlowActivity extends FlowActivity {
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavDrawerAdapter mNavDrawerAdapter;
    private ArrayList<NavDrawerItem> mDrawerItems;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_main);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerItems = new ArrayList<NavDrawerItem>();
        mDrawerItems.add(new NavDrawerItem("Profile", R.drawable.drawer_profile_icon));
        mDrawerItems.add(new NavDrawerItem("Explore", R.drawable.drawer_explore_icon));
        mDrawerItems.add(new NavDrawerItem("Shortlist", R.drawable.drawer_shortlist_icon));
        mDrawerItems.add(new NavDrawerItem("About", R.drawable.drawer_about_icon));

        mNavDrawerAdapter = new NavDrawerAdapter(mDrawerItems, this);
        mDrawerList.setAdapter(mNavDrawerAdapter);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer_am,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }
            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        Fragment profileFrag = new ProfileFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content_frame, profileFrag)
                .commit();

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setItemChecked(0, true);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        getActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
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
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}