package com.uwflow.flow_android.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfileCoursesAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.UserCourseDetail;

public class ProfileCourseFragment extends Fragment {
    private boolean mIsUserMe;

    private ExpandableListView mCoursesListView;
    private TextView mEmptyCoursesView;
    private View rootView;

    private ProfileCoursesAdapter profileListAdapter;
    private ProfileCourseReceiver profileCourseReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_course_layout, container, false);
        mCoursesListView = (ExpandableListView) rootView.findViewById(R.id.course_list);
        mEmptyCoursesView = (TextView) rootView.findViewById(R.id.empty_profile_courses);

        mIsUserMe = getArguments().getBoolean("isUserMe", false);

        // call this before setting up receiver
        populateData();
        profileCourseReceiver = new ProfileCourseReceiver();
        // register receiver
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(profileCourseReceiver,
                new IntentFilter(Constants.BroadcastActionId.UPDATE_PROFILE_USER_COURSES));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).unregisterReceiver(profileCourseReceiver);
        super.onDestroyView();
    }

    protected void populateData() {
        final ProfileFragment profileFragment = ProfileFragment.convertFragment(getParentFragment());
        if (profileFragment == null)
            return;
        UserCourseDetail userCourseDetail = profileFragment.getUserCourses();
        if (userCourseDetail != null && !userCourseDetail.getUserCourses().isEmpty()) {
            toggleShowCourses(true);
            profileListAdapter = new ProfileCoursesAdapter(
                    mIsUserMe,
                    userCourseDetail,
                    getActivity(),
                    getActivity().getSupportFragmentManager());
            mCoursesListView.setAdapter(profileListAdapter);
            profileListAdapter.notifyDataSetChanged();
            expandAllGroups(mCoursesListView);
        } else {
            toggleShowCourses(false);
        }
    }

    private void toggleShowCourses(boolean shouldShow) {
        if (shouldShow) {
            mCoursesListView.setVisibility(View.VISIBLE);
            mEmptyCoursesView.setVisibility(View.GONE);
        } else {
            mCoursesListView.setVisibility(View.GONE);
            mEmptyCoursesView.setVisibility(View.VISIBLE);
        }
    }

    protected class ProfileCourseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            populateData();
        }
    }

    private void expandAllGroups(ExpandableListView listView) {
        for (int i = 0; i < listView.getExpandableListAdapter().getGroupCount(); i++) {
            listView.expandGroup(i);
        }
    }
}
