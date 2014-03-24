package com.uwflow.flow_android;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.facebook.Session;
import com.uwflow.flow_android.adapters.NavDrawerAdapter;
import com.uwflow.flow_android.entities.NavDrawerItem;
import com.uwflow.flow_android.fragment.AboutFragment;
import com.uwflow.flow_android.fragment.ExploreFragment;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.network.FlowAsyncClient;

import java.util.ArrayList;

public class MainFlowActivity extends FlowActivity {
    private static final String TAG = "MainFlowActivity";

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerContainer;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mBottomItemLayout;
    private NavDrawerAdapter mNavDrawerAdapter;
    private ArrayList<NavDrawerItem> mDrawerItems;

    private static final String PROFILE_ITEM_TEXT = "Profile";
    private static final String EXPLORE_ITEM_TEXT = "Explore";
    private static final String ABOUT_ITEM_TEXT = "About";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_main);

        mDrawerContainer = (LinearLayout) findViewById(R.id.drawer);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mBottomItemLayout = (LinearLayout) findViewById(R.id.drawer_bottom_item);

        boolean isUserLoggedIn = ((FlowApplication)getApplication()).isUserLoggedIn();

        mDrawerItems = new ArrayList<NavDrawerItem>();

        if (isUserLoggedIn) {
            mDrawerItems.add(new NavDrawerItem(PROFILE_ITEM_TEXT, R.drawable.drawer_profile_icon));
        }

        mDrawerItems.add(new NavDrawerItem(EXPLORE_ITEM_TEXT, R.drawable.drawer_explore_icon));
        mDrawerItems.add(new NavDrawerItem(ABOUT_ITEM_TEXT, R.drawable.drawer_about_icon));

        // Configure bottom menu item
        TextView label = (TextView) mBottomItemLayout.findViewById(R.id.drawer_item_name);
        ImageView icon = (ImageView) mBottomItemLayout.findViewById(R.id.drawer_item_icon);

        if (isUserLoggedIn) {
            label.setText("Log out");
            icon.setImageResource(R.drawable.drawer_log_out_icon);
            mBottomItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Session.getActiveSession() != null) {
                        Session.getActiveSession().closeAndClearTokenInformation();
                    }
                    Session.setActiveSession(null);

                    // Remove flow cookies
                    FlowAsyncClient.clearCookie();

                    ((FlowApplication) getApplication()).setUserLoggedIn(false);

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
        } else {
            label.setText("Log in");
            icon.setImageResource(R.drawable.drawer_log_in_icon);
            mBottomItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainFlowActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

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
        if (isUserLoggedIn) {
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
            NavDrawerItem drawerItem = (NavDrawerItem) parent.getItemAtPosition(position);
            String itemName = drawerItem.getName();
            Fragment fragment;

            if (itemName.equals(PROFILE_ITEM_TEXT)) {
                fragment = new ProfileFragment();
            } else if (itemName.equals(EXPLORE_ITEM_TEXT)) {
                fragment = new ExploreFragment();
            } else if (itemName.equals(ABOUT_ITEM_TEXT)) {
                fragment = new AboutFragment();
            } else {
                Log.e(TAG, "Unrecognized drawer item selected.");
                return;
            }

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

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0){
             super.onBackPressed();
        }
    }
}
