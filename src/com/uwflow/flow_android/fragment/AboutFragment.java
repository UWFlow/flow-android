package com.uwflow.flow_android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.constant.Constants;
import com.uwflow.flow_android.db_object.*;
import com.uwflow.flow_android.network.FlowApiRequestCallbackAdapter;
import com.uwflow.flow_android.network.FlowApiRequests;
import com.uwflow.flow_android.network.FlowImageLoader;
import com.uwflow.flow_android.util.FacebookUtilities;
import org.json.JSONObject;


public class AboutFragment extends Fragment implements View.OnClickListener {
    private FlowImageLoader flowImageLoader;

    private ImageView profilePic1;
    private ImageView profilePic2;
    private ImageView profilePic3;
    private ImageView profilePic4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.about_layout, container, false);

        profilePic1 = (ImageView)rootView.findViewById(R.id.user_image_1);
        profilePic2 = (ImageView)rootView.findViewById(R.id.user_image_2);
        profilePic3 = (ImageView)rootView.findViewById(R.id.user_image_3);
        profilePic4 = (ImageView)rootView.findViewById(R.id.user_image_4);

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

        profilePic1.setOnClickListener(this);
        profilePic2.setOnClickListener(this);
        profilePic3.setOnClickListener(this);
        profilePic4.setOnClickListener(this);
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
        }
        FacebookUtilities.viewUserOnFacebook(getActivity(), fbid);
    }
}
