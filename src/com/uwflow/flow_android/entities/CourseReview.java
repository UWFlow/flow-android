package com.uwflow.flow_android.entities;

import android.graphics.Bitmap;

/**
 * Created by jasperfung on 2/22/14.
 */
public class CourseReview {
    private String name;
    private String date;
    private String review;
    private Bitmap image;
    private boolean useful;
    private boolean easy;
    private boolean likedIt;

    public CourseReview(String name, String date, String review, Bitmap image, boolean useful, boolean easy, boolean likedIt) {
        this.name = name;
        this.date = date;
        this.review = review;
        this.image = image;
        this.useful = useful;
        this.easy = easy;
        this.likedIt = likedIt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isUseful() {
        return useful;
    }

    public void setUseful(boolean useful) {
        this.useful = useful;
    }

    public boolean isEasy() {
        return easy;
    }

    public void setEasy(boolean easy) {
        this.easy = easy;
    }

    public boolean isLikedIt() {
        return likedIt;
    }

    public void setLikedIt(boolean likedIt) {
        this.likedIt = likedIt;
    }
}
