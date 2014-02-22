package com.uwflow.flow_android.fragment;

//import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import com.uwflow.flow_android.R;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseFragment extends Fragment {
    private FragmentTabHost mTabHost;
                 private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.course_layout, container, false);
        setupTabs();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);



    }

    private View setupTabs() {
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        TabHost.TabSpec tab1 = mTabHost.newTabSpec("schedule")
            .setIndicator("Schedule");
        mTabHost.addTab(tab1, CourseScheduleFragment.class, null);

        TabHost.TabSpec tab2 = mTabHost.newTabSpec("about")
            .setIndicator("About");
        mTabHost.addTab(tab2, CourseAboutFragment.class, null);

        TabHost.TabSpec tab3 = mTabHost.newTabSpec("reviews")
            .setIndicator("Reviews");
        mTabHost.addTab(tab3, CourseReviewsFragment.class, null);

        return mTabHost;
    }
}
