package com.uwflow.flow_android.activities;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import com.uwflow.flow_android.FlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.db_object.ScheduleImage;
import com.uwflow.flow_android.network.FlowDatabaseLoader;
import com.uwflow.flow_android.util.HelperUtil;

public class FullScreenImageActivity extends FlowActivity {
    private ImageView mImageView;

    private FlowDatabaseLoader mFlowDatabaseLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFlowDatabaseLoader = new FlowDatabaseLoader(getApplicationContext(), getHelper());
        getActionBar().hide();


        setContentView(R.layout.full_screen_image_layout);
        mImageView = (ImageView) findViewById(R.id.image_full_screen);

        String profileID = getIntent().getStringExtra("id");

        ScheduleImage scheduleImage = mFlowDatabaseLoader.queryUserScheduleImage(profileID);
        if (scheduleImage != null) {
            Bitmap bitmap = scheduleImage.getImage();
            if (bitmap != null) {
                mImageView.setImageBitmap(HelperUtil.rotateBitmap(bitmap, 90));
            }
        }
    }
}
