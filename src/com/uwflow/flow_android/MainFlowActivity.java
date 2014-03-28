package com.uwflow.flow_android;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.facebook.Session;
import com.uwflow.flow_android.adapters.NavDrawerAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.entities.NavDrawerItem;
import com.uwflow.flow_android.fragment.AboutFragment;
import com.uwflow.flow_android.fragment.CourseFragment;
import com.uwflow.flow_android.fragment.ExploreFragment;
import com.uwflow.flow_android.fragment.ProfileFragment;
import com.uwflow.flow_android.network.FlowAsyncClient;
import com.uwflow.flow_android.nfc.SharableURL;
import org.json.JSONException;
import org.json.JSONObject;

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

    private NfcAdapter mNfcAdapter;

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
            mDrawerItems.add(new NavDrawerItem(
                    Constants.NAV_DRAWER_PROFILE_INDEX,
                    getString(R.string.drawer_item_profile),
                    R.drawable.drawer_profile_icon));
        }

        mDrawerItems.add(new NavDrawerItem(
                Constants.NAV_DRAWER_EXPLORE_INDEX,
                getString(R.string.drawer_item_explore),
                R.drawable.drawer_explore_icon));
        mDrawerItems.add(new NavDrawerItem(
                Constants.NAV_DRAWER_ABOUT_INDEX,
                getString(R.string.drawer_item_about),
                R.drawable.drawer_about_icon));

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

                    FlowApplication app = (FlowApplication) getApplication();
                    app.setUserLoggedIn(false);
                    app.track("Logout");

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
                    ((FlowApplication) getApplication()).track("Login intent from logged out user");

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

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        if (savedInstanceState == null) {
            Fragment initialFragment;
            if (isUserLoggedIn) {
                selectItem(Constants.NAV_DRAWER_PROFILE_INDEX);
            } else {
                selectItem(Constants.NAV_DRAWER_EXPLORE_INDEX);
            }
        }

        // Check for available NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // TODO: Log that NFC is unavailable on this device?
        } else {
            // Register NFC push message callback
            mNfcAdapter.setNdefPushMessageCallback(new NfcAdapter.CreateNdefMessageCallback() {
                @Override
                public NdefMessage createNdefMessage(NfcEvent event) {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                    if (fragment != null && fragment instanceof SharableURL) {
                        String url = ((SharableURL) fragment).getUrl();
                        if (url == null) return null;

                        NdefMessage msg = new NdefMessage(
                                new NdefRecord[] {
                                        NdefRecord.createUri(url)
                                }
                        );

                        JSONObject properties = new JSONObject();
                        try {
                            properties.put("uri", url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((FlowApplication) getApplication()).track("NFC push event", properties);

                        return msg;
                    }
                    return null;
                }
            }, this);
        }

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

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null) {
            // If system supports NFC...
            enableForegroundDispatch(this, mNfcAdapter);

            // See if the Activity is being started/resumed due to an NFC event and handle it
            handleNfcIntent(getIntent());
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null) {
            // If system supports NFC...
            mNfcAdapter.disableForegroundDispatch(this);
        }

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            NavDrawerItem drawerItem = (NavDrawerItem) parent.getItemAtPosition(position);
            selectItem(drawerItem.getId());
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
            case R.id.action_refresh:
                Intent intent = new Intent(Constants.BroadcastActionId.UPDATE_CURRENT_FRAGMENT);
                LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcast(intent);
                break;
            case R.id.action_search:
                selectItem(Constants.NAV_DRAWER_EXPLORE_INDEX);
                break;
                // TODO: mark the Explore page as checked in the nav drawer
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0){
             super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleNfcIntent(intent);
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int itemID) {
        Fragment fragment;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);

        switch (itemID) {
            case(Constants.NAV_DRAWER_PROFILE_INDEX) :
                if (currentFragment != null && currentFragment instanceof ProfileFragment) {
                    if (((ProfileFragment)currentFragment).getProfileID() == null) {
                        // Do nothing. Signed-in user's profile is already onscreen.
                        mDrawerLayout.closeDrawer(mDrawerContainer);
                        return;
                    }
                }
                fragment = new ProfileFragment();
                break;
            case(Constants.NAV_DRAWER_EXPLORE_INDEX) :
                if (currentFragment != null && currentFragment instanceof ExploreFragment) {
                    // Do nothing. Explore fragment is already onscreen.
                    mDrawerLayout.closeDrawer(mDrawerContainer);
                    return;
                }
                fragment = new ExploreFragment();
                break;
            case(Constants.NAV_DRAWER_ABOUT_INDEX) :
                if (currentFragment != null && currentFragment instanceof AboutFragment) {
                    // Do nothing. About fragment is already onscreen.
                    mDrawerLayout.closeDrawer(mDrawerContainer);
                    return;
                }
                fragment = new AboutFragment();
                break;
            default :
                Log.e(TAG, "Unrecognized drawer item selected.");
                return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
        int selectedPosition = mNavDrawerAdapter.getPositionFromId(itemID);
        if (selectedPosition >= 0) {
            mDrawerList.setItemChecked(selectedPosition, true);
        }
        mDrawerLayout.closeDrawer(mDrawerContainer);
    }

    private void handleNfcIntent(Intent intent) {
        if (intent == null) return;

        // Check if Activity was opened via NFC to load a specific Fragment
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

            String payload = intent.getDataString();

            if (payload != null) {

                String profileUrlFormat = Constants.BASE_URL + Constants.URL_PROFILE_EXT;
                String courseUrlFormat = Constants.BASE_URL + Constants.URL_COURSE_EXT;

                if (payload.startsWith(profileUrlFormat)) {
                    // Open a ProfileFragment with the provided payload profile ID

                    String profileID = payload.substring(profileUrlFormat.length());
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, ProfileFragment.newInstance(profileID))
                            .addToBackStack(null)
                            .commit();
                } else if (payload.startsWith(courseUrlFormat)) {
                    // Open a CourseFragment with the provided payload course ID

                    String courseID = payload.substring(courseUrlFormat.length());
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, CourseFragment.newInstance(courseID))
                            .addToBackStack(null)
                            .commit();
                }

                JSONObject properties = new JSONObject();
                try {
                    properties.put("uri", payload);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((FlowApplication) getApplication()).track("NFC receive event", properties);
            }
        }
    }

    /**
     * @param activity The activity that's requesting dispatch
     * @param adapter NfcAdapter for the current context
     */
    private void enableForegroundDispatch(Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        filters[0].addDataScheme("https");
        filters[0].addDataAuthority(Constants.FLOW_DOMAIN, null);
        filters[0].addDataPath(".*", PatternMatcher.PATTERN_SIMPLE_GLOB);

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }
}
