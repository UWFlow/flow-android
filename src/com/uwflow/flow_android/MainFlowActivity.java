package com.uwflow.flow_android;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.facebook.Session;
import com.uwflow.flow_android.adapters.NavDrawerAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.entities.NavDrawerItem;
import com.uwflow.flow_android.fragment.AboutFragment;
import com.uwflow.flow_android.fragment.ExploreFragment;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.fragment.ShortlistFragment;
import com.uwflow.flow_android.network.FlowAsyncClient;

import java.util.ArrayList;

public class MainFlowActivity extends FlowActivity {
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerContainer;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mLogOutLayout;
    private NavDrawerAdapter mNavDrawerAdapter;
    private ArrayList<NavDrawerItem> mDrawerItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_main);

        mDrawerContainer = (LinearLayout) findViewById(R.id.drawer);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLogOutLayout = (LinearLayout) findViewById(R.id.log_out_item);

        mDrawerItems = new ArrayList<NavDrawerItem>();
        mDrawerItems.add(new NavDrawerItem("Profile", R.drawable.drawer_profile_icon));
        mDrawerItems.add(new NavDrawerItem("Explore", R.drawable.drawer_explore_icon));
        mDrawerItems.add(new NavDrawerItem("Shortlist", R.drawable.drawer_shortlist_icon));
        mDrawerItems.add(new NavDrawerItem("About", R.drawable.drawer_about_icon));

        // Configure log out button
        TextView label = (TextView) mLogOutLayout.findViewById(R.id.drawer_item_name);
        ImageView icon = (ImageView) mLogOutLayout.findViewById(R.id.drawer_item_icon);
        label.setText("Log out");
        icon.setImageResource(R.drawable.drawer_log_out_icon);
        mLogOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Session.getActiveSession() != null) {
                    Session.getActiveSession().closeAndClearTokenInformation();
                }
                Session.setActiveSession(null);

                // Remove flow cookies
                FlowAsyncClient.clearCookie();

                ((FlowApplication)getApplication()).setUserLoggedIn(false);

                Intent intent = new Intent(MainFlowActivity.this, LoginActivity.class);
                intent.putExtra("finish", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return;
            }
        });

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

        Fragment initialFragment;
        if (((FlowApplication)getApplication()).isUserLoggedIn()) {
            initialFragment = new ProfileFragment();
        } else {
            initialFragment = new ExploreFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.content_frame, initialFragment)
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
            case R.id.action_search:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new ExploreFragment())
                        .addToBackStack(null)
                        .commit();
                // TODO: mark the Explore page as checked in the nav drawer
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;
        switch (position){
            case(Constants.NAV_DRAWER_PROFILE_INDEX) :
                fragment = new ProfileFragment();
                break;
            case(Constants.NAV_DRAWER_EXPLORE_INDEX) :
                fragment = new ExploreFragment();
                break;
            case(Constants.NAV_DRAWER_SHORTLIST_INDEX) :
                // TODO: implement ShortlistFragment
//                fragment = new ShortlistFragment();
                fragment = ProfileFragment.newInstance(null, Constants.PROFILE_COURSES_PAGE_INDEX);
                break;
            case(Constants.NAV_DRAWER_ABOUT_INDEX) :
                fragment = new AboutFragment();
                break;
        }

        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null)
                    .commit();
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerContainer);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0){
             super.onBackPressed();
        }
    }
}
