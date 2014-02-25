package com.uwflow.flow_android.fragment;

import android.graphics.Bitmap;
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
import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
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
    protected PagerSlidingTabStrip tabs;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.profile_layout, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userImage = (ImageView) getActivity().findViewById(R.id.user_image);
        userName = (TextView) getActivity().findViewById(R.id.user_name);
        userProgram = (TextView) getActivity().findViewById(R.id.user_program);
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
        viewPager.setAdapter(new ProfilePagerAdapter(getActivity().getSupportFragmentManager()));
        tabs = (PagerSlidingTabStrip) getActivity().findViewById(R.id.pager_tabs);
        tabs.setViewPager(viewPager);

        FlowApiRequests.searchUser(new FlowApiRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d("WENTAO", response.toString());

                try {
                    userName.setText(response.getString("name"));
                    //userProgram.setText(response.getString("program_name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    String url = response.getJSONObject("profile_pic_urls").getString("square");
                    Picasso.with(ProfileFragment.this.getActivity().getApplicationContext()).load(url).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                            userImage.setImageBitmap(bitmap);
                        }

                        @Override
                        public void onBitmapFailed() {

                        }
                    });
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

    private static class ProfilePagerAdapter extends FragmentStatePagerAdapter {
        private static final String[] TITLES = new String[] {
                "Friends",
                "Schedule",
                "Exams",
                "Courses"
        };

        public ProfilePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 : return new ProfileFriendFragment();
                case 1 : return new ProfileScheduleFragment();
                case 2 : return new ProfileExamFragment();
                case 3 : return new ProfileCourseFragment();
                default: return new AboutFragment();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
