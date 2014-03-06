package com.uwflow.flow_android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.CourseClassListAdapter;
import com.uwflow.flow_android.adapters.ProfileFriendAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.*;
import com.uwflow.flow_android.loaders.UserFriendsLoader;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;
import com.uwflow.flow_android.util.DateHelper;

import java.util.List;

public class ProfileFriendFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<User>>{
    private String mProfileID;
    protected ListView mProfileFriendList;
    protected View rootView;
    protected ProfileFriendAdapter mProfileFriendAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
	mProfileID = getArguments() != null ? getArguments().getString(Constants.PROFILE_ID_KEY) : null;

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_friend_layout, container, false);
        mProfileFriendList = (ListView)rootView.findViewById(R.id.friend_list);

	if (mProfileID == null) {
	    getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_FRIENDS_LOADER_ID, null, this);
	} else {
	    fetchFriends(mProfileID);
	}

	return rootView;
    }

    @Override
    public Loader<List<User>> onCreateLoader(int i, Bundle bundle) {
        return new UserFriendsLoader(getActivity(), ((MainFlowActivity)getActivity()).getHelper());
    }

    @Override
    public void onLoadFinished(Loader<List<User>> listLoader, List<User> users) {
        mProfileFriendAdapter = new ProfileFriendAdapter(users, getActivity(), getActivity().getSupportFragmentManager());
        mProfileFriendList.setAdapter(mProfileFriendAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<User>> listLoader) {
        mProfileFriendList.setAdapter(null);
    }

    private void fetchFriends(String id){
        if (id == null) return;

        FlowApiRequests.getUserFriends(
                id,
                new FlowApiRequestCallbackAdapter() {
                    @Override
                    public void getUserFriendsCallback(UserFriends userFriends) {
                        List<User> friendList = userFriends.getFriends();

                        mProfileFriendAdapter = new ProfileFriendAdapter(friendList, getActivity(), getActivity().getSupportFragmentManager());
                        mProfileFriendList.setAdapter(mProfileFriendAdapter);
                        mProfileFriendAdapter.notifyDataSetChanged();
                    }
                });
    }

}
