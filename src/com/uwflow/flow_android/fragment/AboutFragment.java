package com.uwflow.flow_android.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.network.FlowImageLoader;
import com.uwflow.flow_android.util.FacebookUtilities;


public class AboutFragment extends TrackedFragment implements View.OnClickListener {
    private FlowImageLoader flowImageLoader;

    private ImageView profilePic1;
    private ImageView profilePic2;
    private ImageView profilePic3;
    private ImageView profilePic4;
    private Button UWFlowButton;
    private TextView versionNameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.about_layout, container, false);

        profilePic1 = (ImageView)rootView.findViewById(R.id.user_image_1);
        profilePic2 = (ImageView)rootView.findViewById(R.id.user_image_2);
        profilePic3 = (ImageView)rootView.findViewById(R.id.user_image_3);
        profilePic4 = (ImageView)rootView.findViewById(R.id.user_image_4);
        UWFlowButton = (Button)rootView.findViewById(R.id.uwflow_button);
        versionNameTextView = (TextView)rootView.findViewById(R.id.version_name);

        flowImageLoader = new FlowImageLoader(getActivity().getApplicationContext());

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flowImageLoader.loadImageInto(String.format(Constants.FB_PROFILE_PIC_LARGE_URL_FORMAT, Constants.FBID_DAVID), profilePic1);
        flowImageLoader.loadImageInto(String.format(Constants.FB_PROFILE_PIC_LARGE_URL_FORMAT, Constants.FBID_JASPER), profilePic2);
        flowImageLoader.loadImageInto(String.format(Constants.FB_PROFILE_PIC_LARGE_URL_FORMAT, Constants.FBID_WENTAO), profilePic3);
        flowImageLoader.loadImageInto(String.format(Constants.FB_PROFILE_PIC_LARGE_URL_FORMAT, Constants.FBID_CHINMAY), profilePic4);

        // Make profile pictures clickable
        profilePic1.setOnClickListener(this);
        profilePic2.setOnClickListener(this);
        profilePic3.setOnClickListener(this);
        profilePic4.setOnClickListener(this);

        // Make uwflow.com button clickable
        UWFlowButton.setOnClickListener(this);

        // Fetch and load current version number into TextView
        try {
            versionNameTextView.setText(String.format("Version: %s",
                    getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(Constants.UW_FLOW, "Couldn't resolve version name: " + e);
        }
    }

    @Override
    public void onClick(View v) {
        String fbid = null;
        switch(v.getId()) {
            case R.id.user_image_1:
                fbid = Constants.FBID_DAVID;
                break;
            case R.id.user_image_2:
                fbid = Constants.FBID_JASPER;
                break;
            case R.id.user_image_3:
                fbid = Constants.FBID_WENTAO;
                break;
            case R.id.user_image_4:
                fbid = Constants.FBID_CHINMAY;
                break;
            case R.id.uwflow_button:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://uwflow.com")));
                return;
        }
        FacebookUtilities.viewUserOnFacebook(getActivity(), fbid);
    }
}
