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
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfileCoursesAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.UserCourseDetail;

public class ProfileCourseFragment extends Fragment {
    protected ExpandableListView mCoursesListView;
    protected View rootView;
    protected ProfileCoursesAdapter profileListAdapter;
    protected ProfileCourseReceiver profileCourseReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_course_layout, container, false);
        mCoursesListView = (ExpandableListView) rootView.findViewById(R.id.course_list);

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
        final Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof ProfileFragment) {
            UserCourseDetail userCourseDetail = ((ProfileFragment) getParentFragment()).getUserCourses();
            if (userCourseDetail != null) {
                profileListAdapter = new ProfileCoursesAdapter(
                        userCourseDetail,
                        getActivity(),
                        getActivity().getSupportFragmentManager());
                mCoursesListView.setAdapter(profileListAdapter);
                profileListAdapter.notifyDataSetChanged();
                expandAllGroups(mCoursesListView);
            }
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
