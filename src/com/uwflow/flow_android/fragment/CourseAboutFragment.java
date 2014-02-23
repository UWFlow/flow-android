package com.uwflow.flow_android.fragment;

//import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.FriendListAdapter;
import com.uwflow.flow_android.entities.Friend;

import java.util.ArrayList;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseAboutFragment extends Fragment {
    private ListView mFriendListView;
    private LinearLayout mFriendLinearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.course_about, container, false);

//        mFriendListView = (ListView)rootView.findViewById(R.id.friend_list);
        mFriendLinearLayout = (LinearLayout)rootView.findViewById(R.id.friend_list);

        // TODO: replace this arraylist with whatever real data source
        ArrayList<Friend> friendList = new ArrayList<Friend>();
        for (int i = 0; i < 5; i++) {
            String first = "Username" + i;
            String second = "Sept " + (2014 + i);
            friendList.add(new Friend(first, second, null));
        }

        FriendListAdapter friendListAdapter = new FriendListAdapter(friendList, getActivity());
//        mFriendListView.setAdapter(friendListAdapter);

        /*
            PROBLEM: listview cannot be placed inside scrollview, since it becomes unscrollable. Two solutions:
            1. Adjust the listview height on every update such that it's always fully expanded
            2. Generate LinearLayouts for every list item

         */

        // Option 2
        int friendCount = friendListAdapter.getCount();
        for (int i = 0; i < friendCount; i++) {
            View item = friendListAdapter.getView(i, null, null);
            mFriendLinearLayout.addView(item);
        }



        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }
}
