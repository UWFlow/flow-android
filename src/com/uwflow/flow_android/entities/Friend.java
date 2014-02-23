package com.uwflow.flow_android.entities;

import android.graphics.Bitmap;

/**
 * Created by jasperfung on 2/23/14.
 */
public class Friend {
    private String first, second;
    private Bitmap image;

    public Friend(String first, String second, Bitmap image) {
        this.first = first;
        this.second = second;
        this.image = image;
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
}
