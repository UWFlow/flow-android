package com.uwflow.flow_android.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfileFriendAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.loaders.UserFriendsLoader;

import java.util.List;

public class ProfileFriendFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<User>>{

    protected ListView mProfileFriendList;
    protected View rootView;
    protected ProfileFriendAdapter profileFriendAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.profile_friend_layout, container, false);
        mProfileFriendList = (ListView)rootView.findViewById(R.id.friend_list);

        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_FRIENDS_LOADER_ID, null, this);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<List<User>> onCreateLoader(int i, Bundle bundle) {
        return new UserFriendsLoader(getActivity(), ((MainFlowActivity)getActivity()).getHelper());
    }

    @Override
    public void onLoadFinished(Loader<List<User>> listLoader, List<User> users) {
	profileFriendAdapter = new ProfileFriendAdapter(users, getActivity(), getFragmentManager());
        mProfileFriendList.setAdapter(profileFriendAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<User>> listLoader) {
        mProfileFriendList.setAdapter(null);
    }
}
