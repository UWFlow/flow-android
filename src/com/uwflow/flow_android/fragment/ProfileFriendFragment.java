package com.uwflow.flow_android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfileFriendAdapter;
import com.uwflow.flow_android.entities.Friend;

import java.util.ArrayList;

public class ProfileFriendFragment extends Fragment {

    protected ListView mProfileFriendList;
    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_friend_layout, container, false);
        mProfileFriendList = (ListView)rootView.findViewById(R.id.friend_list);

        // TODO: replace this arraylist with whatever real data source
        ArrayList<Friend> friendsList = new ArrayList<Friend>();
        for (int i = 0; i < 7; i++) {
            String name = "BestFriend" + i;
            String date = i+" Mutual Classes";
            friendsList.add(new Friend(name, date, null, 10));
        }

        mProfileFriendList.setAdapter(new ProfileFriendAdapter(friendsList, getActivity()));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }
}
