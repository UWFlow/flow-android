package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

/**
 * A base fragment that supports analytics logging. Adapted from http://stackoverflow.com/questions/14883613
 */
public abstract class TrackedFragment extends Fragment {
    private Tracker mTracker;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTracker = EasyTracker.getInstance(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.set(Fields.SCREEN_NAME, ((Object) this).getClass().getSimpleName());
        mTracker.send(MapBuilder.createAppView().build());
    }
}
