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
import android.widget.ListView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfileFriendAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.UserFriends;

public class ProfileFriendFragment extends Fragment {
    protected ListView mProfileFriendList;
    protected View rootView;
    protected ProfileFriendAdapter mProfileFriendAdapter;
    protected ProfileFriendReceiver profileFriendReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_friend_layout, container, false);
        mProfileFriendList = (ListView) rootView.findViewById(R.id.friend_list);
        populateData();
        profileFriendReceiver = new ProfileFriendReceiver();
        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).registerReceiver(profileFriendReceiver,
                new IntentFilter(Constants.BroadcastActionId.UPDATE_PROFILE_USER_FRIENDS));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this.getActivity().getApplicationContext()).unregisterReceiver(profileFriendReceiver);
        super.onDestroyView();
    }

    protected void populateData() {
        final ProfileFragment profileFragment = ProfileFragment.convertFragment(getParentFragment());
        if (profileFragment == null)
            return;
        UserFriends friends = profileFragment.getUserFriends();
        if (friends != null) {
            mProfileFriendAdapter = new ProfileFriendAdapter(friends.getFriends(),
                    getActivity(), getActivity().getSupportFragmentManager());
            mProfileFriendList.setAdapter(mProfileFriendAdapter);
            mProfileFriendList.invalidate();
        }
    }

    protected class ProfileFriendReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            populateData();
        }
    }
}
