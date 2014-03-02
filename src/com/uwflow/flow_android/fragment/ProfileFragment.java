package com.uwflow.flow_android.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uwflow.flow_android.MainFlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.adapters.ProfilePagerAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.loaders.UserMeLoader;

import java.util.List;

public class ProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<User>> {

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
        userImage = (ImageView) rootView.findViewById(R.id.user_image);
        userName = (TextView) rootView.findViewById(R.id.user_name);
        userProgram = (TextView) rootView.findViewById(R.id.user_program);
        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_LOADER_ID, null, this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager.setAdapter(new ProfilePagerAdapter(getActivity().getSupportFragmentManager()));
        tabs = (PagerSlidingTabStrip) getActivity().findViewById(R.id.pager_tabs);
        tabs.setViewPager(viewPager);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public Loader<List<User>> onCreateLoader(int i, Bundle bundle) {
        return new UserMeLoader(getActivity(), ((MainFlowActivity)getActivity()).getHelper());
    }

    @Override
    public void onLoadFinished(Loader<List<User>> listLoader, List<User> users) {
        if (!users.isEmpty()){
            User me = users.get(0);
            userName.setText(me.getName());
            userProgram.setText(me.getProgramName());
            Picasso.with(getActivity()).load(me.getImages()[1]).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    userImage.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed() {

                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<List<User>> listLoader) {
    }
}
