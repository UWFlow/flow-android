package com.uwflow.flow_android.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.UserFriends;
import com.uwflow.flow_android.fragment.ProfileFragment;

public class UserFriendsLoaderCallback implements LoaderManager.LoaderCallbacks<UserFriends>{

    // This is the fragment that contains all other fragments
    protected Fragment parentFragment;
    protected Context context;
    protected FlowDatabaseHelper flowDatabaseHelper;

    public UserFriendsLoaderCallback(Context context, Fragment parentFragment, FlowDatabaseHelper flowDatabaseHelper) {
        this.parentFragment = parentFragment;
        this.context = context;
        this.flowDatabaseHelper = flowDatabaseHelper;
    }

    @Override
    public Loader<UserFriends> onCreateLoader(int i, Bundle bundle) {
        return new UserFriendsLoader(context, flowDatabaseHelper);
    }

    @Override
    public void onLoadFinished(Loader<UserFriends> usersLoader, UserFriends users) {
        if (parentFragment instanceof ProfileFragment){
            final ProfileFragment profileFragment= (ProfileFragment) parentFragment;
            profileFragment.setUserFriends(users);
        }
    }

    @Override
    public void onLoaderReset(Loader<UserFriends> usersLoader) {

    }
}
