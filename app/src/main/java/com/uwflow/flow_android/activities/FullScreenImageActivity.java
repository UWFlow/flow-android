package com.uwflow.flow_android.activities;


import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.widget.ImageView;
import com.uwflow.flow_android.FlowActivity;
import com.uwflow.flow_android.R;
import com.uwflow.flow_android.custom_view.TouchImageView;
import com.uwflow.flow_android.db_object.ScheduleImage;
import com.uwflow.flow_android.network.FlowDatabaseImageCallback;
import com.uwflow.flow_android.network.FlowDatabaseLoader;

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

        mFlowDatabaseLoader.queryUserScheduleImage(profileID, new FlowDatabaseImageCallback() {
            @Override
            public void onScheduleImageLoaded(ScheduleImage scheduleImage) {
                if (scheduleImage != null) {
                    Bitmap bitmap = scheduleImage.getImage();
                    if (bitmap != null) {
                        // When entering fullscreen mode, ensure schedule is full height
                        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                        int statusBarHeight = resourceId > 0 ? getResources().getDimensionPixelSize(resourceId) : 50;
                        Point displaySize = new Point();
                        getWindowManager().getDefaultDisplay().getSize(displaySize);
                        int screenWidth = displaySize.x;
                        int screenHeight = displaySize.y - statusBarHeight;
                        double scaledHeight = bitmap.getHeight() * ((double) screenWidth / bitmap.getWidth());
                        double zoomFactor = (double) screenHeight / scaledHeight;
                        ((TouchImageView) mImageView).setZoom((float) zoomFactor, 0, 0);

                        // TODO(david): Figure out why some schedules get downscaled on Mack's phone
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            }
        });
    }
}
