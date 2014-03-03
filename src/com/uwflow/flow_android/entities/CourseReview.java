package com.uwflow.flow_android.entities;

import android.graphics.Bitmap;

/**
 * Created by jasperfung on 2/22/14.
 */
public class CourseReview {
    private String name;
    private String date;
    private String comment;
    private Bitmap image;
    private Boolean useful;
    private Boolean easy;
    private Boolean likedIt;

    // booleans are nullable
    public CourseReview(String name, String date, String comment, Bitmap image, Boolean useful, Boolean easy, Boolean likedIt) {
        this.name = name;
        this.date = date;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Boolean isUseful() { return useful; }

    public void setUseful(Boolean useful) {
        this.useful = useful;
    }

    public Boolean isEasy() { return easy; }

    public void setEasy(Boolean easy) {
        this.easy = easy;
    }

    public Boolean isLikedIt() { return likedIt; }

    public void setLikedIt(Boolean likedIt) {
        this.likedIt = likedIt;
    }
}
