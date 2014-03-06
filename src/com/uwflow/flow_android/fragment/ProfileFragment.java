package com.uwflow.flow_android.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.uwflow.flow_android.adapters.ProfileFriendAdapter;
import com.uwflow.flow_android.adapters.ProfilePagerAdapter;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.User;
import com.uwflow.flow_android.db_object.UserFriends;
import com.uwflow.flow_android.loaders.UserMeLoader;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;

import java.util.List;

public class ProfileFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<User>> {
    private String mProfileID;
    private ProfilePagerAdapter mProfilePagerAdapter;

    protected ImageView userImage;
    protected TextView userName;
    protected TextView userProgram;
    protected ViewPager viewPager;
    protected PagerSlidingTabStrip tabs;
    private Bitmap imageBitmap;

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

	    mProfilePagerAdapter = new ProfilePagerAdapter(getChildFragmentManager(), getArguments());

	    viewPager.setAdapter(mProfilePagerAdapter);
	    tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.pager_tabs);
	    tabs.setViewPager(viewPager);
        // Set default tab to Schedule
        if (getArguments() != null) {
            viewPager.setCurrentItem(getArguments().getInt(Constants.TAB_ID, Constants.PROFILE_SCHEDULE_PAGE_INDEX));
        }

	    mProfileID = getArguments() != null ? getArguments().getString(Constants.PROFILE_ID_KEY) : null;

	    if (mProfileID == null) {
	    // Load logged-in users profile if an ID is unspecified.
	        getLoaderManager().initLoader(Constants.LoaderManagerId.PROFILE_LOADER_ID, null, this);
	    }
	    return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (imageBitmap != null) userImage.setImageBitmap(imageBitmap);

        fetchProfileInfo(mProfileID);
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
            Picasso.with(getActivity()).load(me.getProfilePicUrls().getLarge()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                        userImage.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {

                }

                @Override
                public void onPrepareLoad(Drawable drawable) {

                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<List<User>> listLoader) {
    }


    private void fetchProfileInfo(String id){
	if (id == null) return;

	FlowApiRequests.getUser(
		id,
		new FlowApiRequestCallbackAdapter() {
		    @Override
		    public void getUserCallback(User user) {

			userName.setText(user.getName());
			userProgram.setText(user.getProgramName());
			Picasso.with(getActivity()).load(user.getProfilePicUrls().getLarge()).into(new Target() {
			    @Override
			    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
				if (userImage != null)
				    userImage.setImageBitmap(bitmap);
				else
				    imageBitmap = bitmap;
			    }

			    @Override
			    public void onBitmapFailed(Drawable drawable) {

			    }

			    @Override
			    public void onPrepareLoad(Drawable drawable) {

			    }
			});
		    }
		});
    }


}
