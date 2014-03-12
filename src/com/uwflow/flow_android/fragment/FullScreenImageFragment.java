package com.uwflow.flow_android.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import com.uwflow.flow_android.R;

import android.support.v4.app.Fragment;
import com.uwflow.flow_android.util.HelperUtil;

public class FullScreenImageFragment extends Fragment {

    private View rootView;
    private ImageView mImageShow;
    private Bitmap bitmapImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.full_screen_image_layout, container, false);
        mImageShow = (ImageView) rootView.findViewById(R.id.image_full_screen);

        bitmapImage = getArguments().getParcelable("ScheduleImage");
        if (bitmapImage != null)
            mImageShow.setImageBitmap(HelperUtil.rotateBitmap(bitmapImage, 90));
        //mImageShow.setScaleType(ImageView.ScaleType.FIT_XY);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
