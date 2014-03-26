package com.uwflow.flow_android.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import com.uwflow.flow_android.dao.FlowDatabaseHelper;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.fragment.ProfileFragment;

public class UserLoaderCallback implements LoaderManager.LoaderCallbacks<User> {
    // This is the fragment that contains all other fragments
    protected Fragment parentFragment;
    protected Context context;
    protected FlowDatabaseHelper flowDatabaseHelper;

    public UserLoaderCallback(Context context, Fragment parentFragment, FlowDatabaseHelper flowDatabaseHelper) {
        this.parentFragment = parentFragment;
        this.context = context;
        this.flowDatabaseHelper = flowDatabaseHelper;
    }

    @Override
    public Loader<User> onCreateLoader(int i, Bundle bundle) {
        return new UserLoader(context, flowDatabaseHelper, parentFragment);
    }

    @Override
    public void onLoadFinished(Loader<User> userLoader, User user) {
        if (user == null)
            return;

        if (parentFragment instanceof ProfileFragment) {
            final ProfileFragment profileFragment = (ProfileFragment) parentFragment;
            profileFragment.setUser(user);
        }
    }

    @Override
    public void onLoaderReset(Loader<User> userLoader) {

    }
}
