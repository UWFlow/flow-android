package com.uwflow.flow_android.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.uwflow.flow_android.R;

public class FlowImageLoader {
    protected Context context;

    public FlowImageLoader(Context context) {
        this.context = context;
    }

    //ONLY CALL THIS IN WORKER THREAD
    public void preloadImage(String url) {
        ImageLoader.getInstance().loadImageSync(url);
    }

    public void clearImageCache(){
        ImageLoader.getInstance().clearDiscCache();
        ImageLoader.getInstance().clearMemoryCache();
    }

    public void loadImage(String url, final ImageView imageView, final FlowImageLoaderCallback callback){
        ImageLoader.getInstance().displayImage(url, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                callback.onImageLoaded(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    public void loadImageForList(String url, final ImageView imageView){
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(R.drawable.kitty)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();
        ImageLoader.getInstance().displayImage(url, imageView, defaultOptions);
    }

    public void loadImage(String url, final FlowImageLoaderCallback callback) {
        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (callback != null)
                    callback.onImageLoaded(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    public void loadImageInto(String url, ImageView imageView) {
        ImageLoader.getInstance().displayImage(url, imageView);
    }

    public void loadImageInto(String url, ImageView imageView, final FlowImageLoaderCallback callback) {
        ImageLoader.getInstance().displayImage(url, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                callback.onImageLoaded(bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }
}
