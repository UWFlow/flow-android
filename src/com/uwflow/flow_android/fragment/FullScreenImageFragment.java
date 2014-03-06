package com.uwflow.flow_android.fragment;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.uwflow.flow_android.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import android.support.v4.app.Fragment;

/**
 * Created by Chinmay on 3/6/14.
 */
public class FullScreenImageFragment extends Fragment {

    private View rootView;
    private ImageView mImageShow;
    private Bitmap bitmapImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.full_screen_image_layout, container, false);
        mImageShow = (ImageView)rootView.findViewById(R.id.image_full_screen);

        bitmapImage = getArguments().getParcelable("ScheduleImage");
        mImageShow.setImageBitmap(bitmapImage);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

}
