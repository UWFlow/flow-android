package com.uwflow.flow_android.entities;

import android.graphics.Bitmap;

/**
 * Created by jasperfung on 2/23/14.
 */
public class Friend {
    private String first, second;
    private String bitmapUrl;
    private Bitmap image;
    private int fbid;

    public Friend(String first, String second, Bitmap image, int fbid) {
        this.first = first;
        this.second = second;
        this.image = image;
        this.fbid = fbid;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getFbid() {
        return fbid;
    }

    public void setFbid(int fbid) {
        this.fbid = fbid;
    }
}
