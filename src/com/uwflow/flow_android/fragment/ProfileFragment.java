package com.uwflow.flow_android.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.facebook.*;
import com.facebook.model.GraphObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfilePagerAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.*;
import com.uwflow.flow_android.loaders.*;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    private String mProfileID;
    private ProfilePagerAdapter mProfilePagerAdapter;

    protected ImageView userImage;
    protected TextView userName;
    protected TextView userProgram;
    protected ViewPager viewPager;
    protected PagerSlidingTabStrip tabs;
    private ImageView mCoverPhoto;
    protected User user;
    protected UserFriends userFriends;
    protected Exams userExams;
    protected UserCourseDetail userCourses;
    protected ScheduleCourses userSchedule;
    protected ProfileReceiver profileReceiver;
    protected Bitmap userCover;
    protected Target coverImageCallback;
    protected Target imageCallback;

    // only fetch data once
    protected boolean fetchCompleted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.profile_layout, container, false);
        mProfileID = getArguments() != null ? getArguments().getString(Constants.PROFILE_ID_KEY) : null;

        userImage = (ImageView) rootView.findViewById(R.id.user_image);
        userName = (TextView) rootView.findViewById(R.id.user_name);
        userProgram = (TextView) rootView.findViewById(R.id.user_program);
        mCoverPhoto = (ImageView) rootView.findViewById(R.id.cover_photo);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        // Note: this is sorta cheating. We might need to decrease this number so that we don't run into memory issues.
        viewPager.setOffscreenPageLimit(3);

        if (mProfileID == null)
            mProfilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager(), getArguments(), true);
        else
            mProfilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager(), getArguments(), false);

        viewPager.setAdapter(mProfilePagerAdapter);
        tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.pager_tabs);
        tabs.setViewPager(viewPager);
        // Set default tab to Schedule
        if (mProfileID == null)
            viewPager.setCurrentItem(Constants.PROFILE_SCHEDULE_PAGE_INDEX);


        profileReceiver = new ProfileReceiver();

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

    protected void fetchProfileInfo() {
        fetchCompleted = true;
        if (mProfileID == null) {
            // Load logged-in users profile if an ID is unspecified.
            initLoaders();
        } else {
            // fetch user profile data from network
            initLoadFromNetwork(mProfileID);
        }
    }

    private void fetchCoverPhoto(long fbid) {
        if (userCover != null) {
            mCoverPhoto.setImageBitmap(userCover);
        } else {
            Bundle params = new Bundle();
            params.putString("fields", "cover");
            new Request(
                    Session.getActiveSession(),
                    "/" + fbid,
                    params,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                            GraphObject graphObject = response.getGraphObject();
                            FacebookRequestError error = response.getError();
                            if (graphObject != null) {
                                if (graphObject.getProperty("cover") != null) {
                                    try {
                                        JSONObject json = graphObject.getInnerJSONObject();
                                        JSONObject coverObject =

                                                new JSONObject((String) json.getString("cover"));
                                        String url = coverObject.getString("source");
//                                        Picasso.with(getActivity().getApplicationContext()).load(url).into(mCoverPhoto,
//                                                new Callback() {
//                                                    @Override
//                                                    public void onSuccess() {
//                                                        userCover = ImageHelper.drawableToBitmap(mCoverPhoto.getDrawable());
//                                                    }
//
//                                                    @Override
//                                                    public void onError() {
//
//                                                    }
//                                                });
                                        coverImageCallback = new Target() {
                                            @Override
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                                                Log.d("BITMAP", "LOADED COVER FROM: " + loadedFrom.toString());
                                                userCover = bitmap;
                                                mCoverPhoto.setImageBitmap(userCover);
                                            }

                                            @Override
                                            public void onBitmapFailed(Drawable drawable) {
                                                Log.d("BITMAP", "NOTLOADED");
                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable drawable) {

                                            }
                                        };
                                        Picasso.with(getActivity().getApplicationContext()).load(url).into(coverImageCallback);
                                    } catch (JSONException e) {
                                    }


                                } else if (error != null) {
                                }
                            }
                        }
                    }
            ).executeAsync();
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

        // Get user exam data
        FlowApiRequests.getUserExams(
                uid,
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getUserExamsCallback(Exams response) {
                        setUserExams(response);
                    }
                });
    }

    protected void populateData() {
        if (user != null) {
            fetchCoverPhoto(user.getFbid());

            imageCallback = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    userImage.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {

                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            };
            // Set profile picture
            Picasso.with(getActivity()).load(user.getProfilePicUrls().getLarge()).into(imageCallback);

            userName.setText(user.getName());
            userProgram.setText(user.getProgramName());
        }
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
}
