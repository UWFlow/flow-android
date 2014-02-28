package com.uwflow.flow_android.fragment;

//import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.FriendListAdapter;
import com.uwflow.flow_android.entities.Friend;

import java.util.ArrayList;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseAboutFragment extends Fragment {
    private LinearLayout mFriendListContainer;
    private BaseAdapter mFriendListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.course_about, container, false);

        mFriendListContainer = (LinearLayout)rootView.findViewById(R.id.friend_list);

        // TODO: replace this arraylist with whatever real data source
        ArrayList<Friend> friendList = new ArrayList<Friend>();
        for (int i = 0; i < 5; i++) {
            String first = "Username " + i;
            String second = "Sept " + (2014 + i);
            friendList.add(new Friend(first, second, null, i + 100));
        }

        mFriendListAdapter = new FriendListAdapter(friendList, getActivity());

        // Generate LinearLayouts for every list item
        for (int i = 0; i < mFriendListAdapter.getCount(); i++) {
            View item = mFriendListAdapter.getView(i, null, null);
            mFriendListContainer.addView(item);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }
}
