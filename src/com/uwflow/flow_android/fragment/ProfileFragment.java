package com.uwflow.flow_android.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfilePagerAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.*;
import com.uwflow.flow_android.loaders.*;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;
import com.uwflow.flow_android.network.FlowImageLoader;
import com.uwflow.flow_android.network.FlowImageLoaderCallback;
import com.uwflow.flow_android.util.FacebookUtilities;

public class ProfileFragment extends Fragment {
    private String mProfileID;
    private ProfilePagerAdapter profilePagerAdapter;

    protected ImageView userPhotoImageView;
    private ImageView coverPhotoImageView;
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
    protected Bitmap userCover;
    protected Bitmap userProfileImage;
    protected FlowImageLoader flowImageLoader;

    // for having hard reference to callbacks so they dont get garbage recycled
    protected FlowImageLoaderCallback userCoverCallback;
    protected FlowImageLoaderCallback userProfileCallback;


    // only fetch data once
    protected boolean fetchCompleted = false;

    /**
     * Static method to instantiate this class with arguments passed as a bundle.
     *
     * @param userId The ID of the user to show.
     * @return A new instance.
     */
    public static ProfileFragment newInstance(String userId) {
        ProfileFragment profileFragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.PROFILE_ID_KEY, userId);
        profileFragment.setArguments(bundle);

        return profileFragment;
    }

    public static ProfileFragment newInstance(User user) {
        ProfileFragment profileFragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.USER, user);
        profileFragment.setArguments(bundle);

        return profileFragment;
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
        user = getArguments() != null ? (User) getArguments().getSerializable(Constants.USER) : null;
        if (user != null)
            mProfileID = user.getId();
        else
            mProfileID = getArguments() != null ? getArguments().getString(Constants.PROFILE_ID_KEY) : null;

        flowImageLoader = new FlowImageLoader(getActivity().getApplicationContext());

        userPhotoImageView = (ImageView) rootView.findViewById(R.id.user_image);
        userName = (TextView) rootView.findViewById(R.id.user_name);
        userProgram = (TextView) rootView.findViewById(R.id.user_program);
        coverPhotoImageView = (ImageView) rootView.findViewById(R.id.cover_photo);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        // Note: this is sorta cheating. We might need to decrease this number so that we don't run into memory issues.
        viewPager.setOffscreenPageLimit(3);

        if (mProfileID == null)
            profilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager(), getArguments(), true);
        else
            profilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager(), getArguments(), false);

        viewPager.setAdapter(profilePagerAdapter);
        tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.pager_tabs);
        tabs.setViewPager(viewPager);
        // Set default tab to Schedule
        if (mProfileID == null)
            viewPager.setCurrentItem(Constants.PROFILE_SCHEDULE_PAGE_INDEX);


        profileReceiver = new ProfileReceiver();
        initCallback();
        populateData();
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(profileReceiver,
                new IntentFilter(Constants.BroadcastActionId.UPDATE_PROFILE_USER));
        if (!fetchCompleted) fetchProfileInfo();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_on_fb:
                if (user != null && user.getFbid() != null) {
                    FacebookUtilities.viewUserOnFacebook(getActivity(), user.getFbid());
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void fetchProfileInfo() {
        fetchCompleted = true;
        if (mProfileID == null) {
            // Load logged-in users profile if an ID is unspecified.
            initLoaders();
        } else {
            initLoadFromNetwork(mProfileID);
        }
    }

    protected void initLoaders() {
        final UserLoaderCallback userLoader = new UserLoaderCallback(getActivity().getApplicationContext(), this, ((MainFlowActivity) getActivity()).getHelper());
        final UserFriendsLoaderCallback userFriendsLoader = new UserFriendsLoaderCallback(getActivity().getApplicationContext(), this, ((MainFlowActivity) getActivity()).getHelper());
        final UserScheduleLoaderCallback userScheduleLoaderCallback = new UserScheduleLoaderCallback(getActivity().getApplicationContext(), this, ((MainFlowActivity) getActivity()).getHelper());
        final UserExamsLoaderCallback userExamsLoaderCallback = new UserExamsLoaderCallback(getActivity().getApplicationContext(), this, ((MainFlowActivity) getActivity()).getHelper());
        final UserCoursesLoaderCallback userCoursesLoaderCallback = new UserCoursesLoaderCallback(getActivity().getApplicationContext(), this, ((MainFlowActivity) getActivity()).getHelper());
        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_LOADER_ID, null, userLoader);
        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_FRIENDS_LOADER_ID, null, userFriendsLoader);
        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_SCHEDULE_LOADER_ID, null, userScheduleLoaderCallback);
        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_EXAMS_LOADER_ID, null, userExamsLoaderCallback);
        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_COURSES_LOADER_ID, null, userCoursesLoaderCallback);
    }

    protected void initLoadFromNetwork(final String uid) {
        if (uid == null)
            return;

        // we might have the cached user data already
        if (user == null) {
            FlowApiRequests.getUser(
                    uid,
                    new FlowApiRequestCallbackAdapter() {
                        @Override
                        public void getUserCallback(User user) {
                            setUser(user);
                        }
                    });
        }

        // Get user data
        FlowApiRequests.getUser(
                uid,
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getUserCallback(User user) {
                        setUser(user);
                    }
                });

        FlowApiRequests.getUserSchedule(
                uid,
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getUserScheduleCallback(ScheduleCourses scheduleCourses) {
                        setUserSchedule(scheduleCourses);
                    }
                });
        FlowApiRequests.getUserExams(
                uid,
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getUserExamsCallback(Exams exams) {
                        setUserExams(exams);
                    }
                });
        FlowApiRequests.getUserCourses(
                uid,
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getUserCoursesCallback(UserCourseDetail userCourseDetail) {
                        setUserCourses(userCourseDetail);
                    }
                });
    }

    protected void initCallback(){
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

        if (userCover == null) {
            if (user.getFbid() != null)
                FlowApiRequests.getUserCoverImage(this.getActivity().getApplicationContext(), user.getFbid(), coverPhotoImageView,
                        userCoverCallback);
        }

        if (userProfileImage == null && user.getProfilePicUrls() != null) {
            flowImageLoader.loadImage(user.getProfilePicUrls().getLarge(), userPhotoImageView, userProfileCallback);

        }

        userName.setText(user.getName());
        userProgram.setText(user.getProgramName());
    }

    protected class ProfileReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            populateData();
        }
    }

    // Getters and Setters for child fragment to pull data
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (this.user == null) {
            this.user = user;
            fireUserBroadcast();
        }
    }

    public UserFriends getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(UserFriends userFriends) {
        if (this.userFriends == null) {
            this.userFriends = userFriends;
            fireUserFriendBroadcast();
        }
    }

    public Exams getUserExams() {
        return userExams;
    }

    public void setUserExams(Exams userExams) {
        if (this.userExams == null) {
            this.userExams = userExams;
            fireUserExamBroadcast();
        }
    }

    public UserCourseDetail getUserCourses() {
        return userCourses;
    }

    public void setUserCourses(UserCourseDetail userCourses) {
        if (this.userCourses == null) {
            this.userCourses = userCourses;
            fireUserCoursesBroadcast();
        }
    }

    public ScheduleCourses getUserSchedule() {
        return userSchedule;
    }

    public void setUserSchedule(ScheduleCourses userSchedule) {
        if (this.userSchedule == null) {
            this.userSchedule = userSchedule;
            fireUserScheduleBroadcast();
        }
    }

    protected void fireUserBroadcast() {
        Intent intent = new Intent(Constants.BroadcastActionId.UPDATE_PROFILE_USER);
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).sendBroadcast(intent);
    }

    protected void fireUserFriendBroadcast() {
        Intent intent = new Intent(Constants.BroadcastActionId.UPDATE_PROFILE_USER_FRIENDS);
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).sendBroadcast(intent);
    }

    protected void fireUserScheduleBroadcast() {
        Intent intent = new Intent(Constants.BroadcastActionId.UPDATE_PROFILE_USER_SCHEDULE);
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).sendBroadcast(intent);
    }

    protected void fireUserExamBroadcast() {
        Intent intent = new Intent(Constants.BroadcastActionId.UPDATE_PROFILE_USER_EXAMS);
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).sendBroadcast(intent);
    }

    protected void fireUserCoursesBroadcast() {
        Intent intent = new Intent(Constants.BroadcastActionId.UPDATE_PROFILE_USER_COURSES);
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).sendBroadcast(intent);
    }

    public String getProfileID() {
        return mProfileID;
    }

    public void setProfileID(String profileId) {
        this.mProfileID = profileId;
    }

    public static ProfileFragment convertFragment(Fragment fragment) {
        try {
            return (ProfileFragment) fragment;
        } catch (Exception e) {
            return null;
        }
    }
}
