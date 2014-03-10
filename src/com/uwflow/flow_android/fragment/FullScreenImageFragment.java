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
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import com.uwflow.flow_android.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.support.v4.app.Fragment;

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
            mImageShow.setImageBitmap(bitmapImage);
        mImageShow.setScaleType(ImageView.ScaleType.FIT_XY);
        return rootView;
    }

    private void rotate(float degree) {
        final RotateAnimation rotateAnim = new RotateAnimation(0.0f, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        mImageShow.startAnimation(rotateAnim);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
