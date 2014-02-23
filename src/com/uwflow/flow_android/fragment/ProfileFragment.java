package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.network.FlowApiRequestCallback;
import com.uwflow.flow_android.network.FlowApiRequests;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {

    protected ImageView userImage;
    protected TextView userName;
    protected TextView userProgram;
    protected ViewPager viewPager;
    private static final int NUM_PAGES = 4;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.profile_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userImage = (ImageView) getActivity().findViewById(R.id.user_image);
        userName = (TextView) getActivity().findViewById(R.id.user_name);
        userProgram = (TextView) getActivity().findViewById(R.id.user_program);
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        viewPager.setAdapter(new ProfileTitlePagerAdapter(getActivity().getSupportFragmentManager()));

        // Bind the tabs to the ViewPager
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) getActivity().findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        getActivity().getSupportFragmentManager();
        FlowApiRequests.searchUser(new FlowApiRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d("WENTAO", response.toString());

                try {
                    userName.setText(response.getString("name"));
                    userProgram.setText(response.getString("program_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String url = response.getJSONObject("profile_pic_urls").getString("square");
                    Picasso.with(ProfileFragment.this.getActivity().getApplicationContext()).load(url).into(userImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    private class ProfileTitlePagerAdapter extends FragmentStatePagerAdapter {
        public ProfileTitlePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new AboutFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Title: " + position;
        }
    }
}
