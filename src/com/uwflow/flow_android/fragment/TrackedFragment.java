package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.uwflow.flow_android.FlowApplication;

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
        String fragmentName = ((Object) this).getClass().getSimpleName();

        // Send data to Google Analytics
        mTracker.set(Fields.SCREEN_NAME, fragmentName);
        mTracker.send(MapBuilder.createAppView().build());

        // Send data to Mixpanel
        ((FlowApplication) getActivity().getApplication()).track("Impression: " + fragmentName);
    }
}
