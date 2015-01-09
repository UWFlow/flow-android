package com.uwflow.flow_android.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.uwflow.flow_android.FlowApplication;
import com.crashlytics.android.Crashlytics;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfilePagerAdapter;
import com.uwflow.flow_android.broadcast_receiver.BroadcastFactory;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.*;
import com.uwflow.flow_android.loaders.*;
import com.uwflow.flow_android.network.*;
import com.uwflow.flow_android.nfc.SharableURL;
import com.uwflow.flow_android.util.FacebookUtilities;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends TrackedFragment implements SharableURL {
    private String mProfileID;
    private ProfilePagerAdapter profilePagerAdapter;
    private MenuItem mFbMenuItem;

    protected ImageView userPhotoImageView;
    protected ImageView coverPhotoImageView;
    protected TextView userName;
    protected TextView userProgram;
    protected ViewPager viewPager;
    protected PagerSlidingTabStrip tabs;
    protected User user;
    protected UserFriends userFriends;
    protected Exams userExams;
    protected UserCourseDetail userCourses;
    protected ScheduleCourses userSchedule;
    protected ProfileReceiver profileReceiver;
    protected ProfileRefreshReceiver profileRefreshReceiver;
    protected Bitmap userCover;
    protected Bitmap userProfileImage;
    protected FlowImageLoader mFlowImageLoader;
    protected FlowDatabaseLoader mFlowDatabaseLoader;
    // for having hard reference to callbacks so they dont get garbage recycled
    protected FlowImageLoaderCallback userCoverCallback;
    protected FlowImageLoaderCallback userProfileCallback;
    protected FlowResultCollector collector;

    /**
     * Static method to instantiate this class with arguments passed as a bundle.
     *
     * @param userID The ID of the user to show.
     * @param tabID  The ID of the initial tab to show
     * @return A new instance.
     */
    public static ProfileFragment newInstance(String userID, Integer tabID) {
        ProfileFragment profileFragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        if (tabID != null) {
            bundle.putInt(Constants.TAB_ID_KEY, tabID);
        }
        if (userID != null) {
            bundle.putString(Constants.PROFILE_ID_KEY, userID);
        }
        profileFragment.setArguments(bundle);

        return profileFragment;
    }

    public static ProfileFragment newInstance(String userId) {
        return newInstance(userId, null);
    }

    public static ProfileFragment newInstance(User user) {
        return newInstance(user.getId());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.profile_layout, container, false);

        userPhotoImageView = (ImageView) rootView.findViewById(R.id.user_image);
        userName = (TextView) rootView.findViewById(R.id.user_name);
        userProgram = (TextView) rootView.findViewById(R.id.user_program);
        coverPhotoImageView = (ImageView) rootView.findViewById(R.id.cover_photo);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        // Note: this is sorta cheating. We might need to decrease this number so that we don't run into memory issues.
        viewPager.setOffscreenPageLimit(3);

        Integer tabID = getArguments() != null ? getArguments().getInt(Constants.TAB_ID_KEY) : null;
        if (tabID == null) {
            // Set default tab to Schedule
            viewPager.setCurrentItem(Constants.PROFILE_SCHEDULE_PAGE_INDEX);
        } else {
            viewPager.setCurrentItem(tabID);
        }

        init();

        if (getArguments() != null) {
            mProfileID = getArguments().getString(Constants.PROFILE_ID_KEY);
        }

        profilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager(), getArguments(), mProfileID == null);

        viewPager.setAdapter(profilePagerAdapter);
        tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.pager_tabs);
        tabs.setViewPager(viewPager);

        initLoaders();
        populateData();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).unregisterReceiver(profileReceiver);
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(profileRefreshReceiver,
                new IntentFilter(Constants.BroadcastActionId.UPDATE_CURRENT_FRAGMENT));
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).unregisterReceiver(profileRefreshReceiver);
        super.onPause();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        mFbMenuItem = menu.findItem(R.id.action_view_on_fb);
        mFbMenuItem.setVisible(true);
    }

    @Override
    public String getUrl() {
        if (user == null || StringUtils.isEmpty(user.getId())) {
            return null;
        }
        return Constants.BASE_URL + Constants.URL_PROFILE_EXT + user.getId();
    }

    protected void initLoaders() {
        final UserLoaderCallback userLoaderCallback = new UserLoaderCallback(getActivity().getApplicationContext(), this, ((MainFlowActivity) getActivity()).getHelper());
        final UserFriendsLoaderCallback userFriendsLoaderCallback = new UserFriendsLoaderCallback(getActivity().getApplicationContext(), this, ((MainFlowActivity) getActivity()).getHelper());
        final UserScheduleLoaderCallback userScheduleLoaderCallback = new UserScheduleLoaderCallback(getActivity().getApplicationContext(), this, ((MainFlowActivity) getActivity()).getHelper());
        final UserExamsLoaderCallback userExamsLoaderCallback = new UserExamsLoaderCallback(getActivity().getApplicationContext(), this, ((MainFlowActivity) getActivity()).getHelper());
        final UserCoursesLoaderCallback userCoursesLoaderCallback = new UserCoursesLoaderCallback(getActivity().getApplicationContext(), this, ((MainFlowActivity) getActivity()).getHelper());
        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_LOADER_ID, null, userLoaderCallback);
        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_FRIENDS_LOADER_ID, null, userFriendsLoaderCallback);
        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_SCHEDULE_LOADER_ID, null, userScheduleLoaderCallback);
        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_EXAMS_LOADER_ID, null, userExamsLoaderCallback);
        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_COURSES_LOADER_ID, null, userCoursesLoaderCallback);
    }

    protected void init() {
        mFlowImageLoader = new FlowImageLoader(getActivity().getApplicationContext());
        mFlowDatabaseLoader = new FlowDatabaseLoader(getActivity().getApplicationContext(), ((MainFlowActivity) getActivity()).getHelper());
        profileReceiver = new ProfileReceiver();
        profileRefreshReceiver = new ProfileRefreshReceiver();
        userCoverCallback = new FlowImageLoaderCallback() {
            @Override
            public void onImageLoaded(Bitmap bitmap) {
                userCover = bitmap;
            }
        };
        userProfileCallback = new FlowImageLoaderCallback() {
            @Override
            public void onImageLoaded(Bitmap bitmap) {
                userProfileImage = bitmap;
            }
        };
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(profileReceiver,
                new IntentFilter(Constants.BroadcastActionId.UPDATE_PROFILE_USER));
    }

    protected void populateData() {
        if (userProfileImage != null) {
            userPhotoImageView.setImageBitmap(userProfileImage);
        }

        if (userCover != null) {
            coverPhotoImageView.setImageBitmap(userCover);
        }

        if (user == null)
            return;

        if (userProfileImage == null && user.getProfilePicUrls() != null) {
            mFlowImageLoader.loadImage(user.getProfilePicUrls().getLarge(), userPhotoImageView, userProfileCallback);

        }

        userName.setText(user.getName());
        userProgram.setText(user.getProgramName());

        if (user.isMe()) {
            // Associate the current user for all future tracking calls to Mixpanel
            // TODO(david): Ideally this should be called as soon as the user logs in. Will need to change server API to
            //     return user info on POST /api/v1/login/facebook.
            FlowApplication app = (FlowApplication) getActivity().getApplication();
            app.getMixpanel().identify(user.getId());

            // Send Crashlytics info about current user (may be helpful in communicating/asking for more info)
            Crashlytics.setUserIdentifier(user.getId());
            Crashlytics.setUserName(user.getName());
        }
    }

    // Getters and Setters for child fragment to pull data
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (user == null) {
            if (mFbMenuItem != null) {
                mFbMenuItem.setEnabled(false);
            }
            return;
        }
        this.user = user;
        if (user.getFbid() != null) {
            if (mFbMenuItem != null) {
                mFbMenuItem.setEnabled(true);
            }

            if (userCover == null) {
                FlowApiRequests.getUserCoverImage(this.getActivity().getApplicationContext(), user.getFbid(), coverPhotoImageView,
                        userCoverCallback);
            }
        }
        BroadcastFactory.fireProfileMeFragmentDataLoaded(this.getActivity().getApplicationContext());
    }

    public UserFriends getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(UserFriends userFriends) {
        this.userFriends = userFriends;
        BroadcastFactory.fireProfileFriendFragmentDataLoaded(this.getActivity().getApplicationContext());
    }

    public Exams getUserExams() {
        return userExams;
    }

    public void setUserExams(Exams userExams) {
        this.userExams = userExams;
        BroadcastFactory.fireProfileExamFragmentDataLoaded(this.getActivity().getApplicationContext());
    }

    public UserCourseDetail getUserCourses() {
        return userCourses;
    }

    public void setUserCourses(UserCourseDetail userCourses) {
        this.userCourses = userCourses;
        BroadcastFactory.fireProfileCourseFragmentDataLoaded(this.getActivity().getApplicationContext());

    }

    public ScheduleCourses getUserSchedule() {
        return userSchedule;
    }

    public void setUserSchedule(ScheduleCourses userSchedule) {
        this.userSchedule = userSchedule;
        BroadcastFactory.fireProfileScheduleFragmentDataLoaded(this.getActivity().getApplicationContext());
    }

    public String getProfileID() {
        return mProfileID;
    }

    public void setProfileID(String profileId) {
        this.mProfileID = profileId;
    }

    /**
     * This class is to listen for the event when the current user is loaded (from database or network)
     * and populate data.
     */
    protected class ProfileReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            populateData();
        }
    }

    /**
     * This class is to listen for the refresh button and reloads all the data
     */
    protected class ProfileRefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            userProfileImage = null;
            userCover = null;
            // TODO maybe insted of clearing the whole cache, we can manually clear part of the cache we want to reload
            mFlowImageLoader.clearImageCache();
            // if not logged in user then just fire async call without loading into database
            if (mProfileID != null) {
                Intent reloadLoadersIntent = new Intent(Constants.BroadcastActionId.UPDATE_PROFILE_LOADER);
                LocalBroadcastManager.getInstance(ProfileFragment.this.getActivity().getApplicationContext()).sendBroadcast(reloadLoadersIntent);
            } else {
                // fire async call and load into database
                mFlowDatabaseLoader.loadOrReloadProfileForLogin(null);
            }
        }
    }
}
