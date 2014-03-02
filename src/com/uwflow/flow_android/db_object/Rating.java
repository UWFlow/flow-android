package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rating implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int USEFULNESS = 0;
    public static final int EASINESS = 1;
    public static final int INTEREST = 2;

    @SerializedName("count")
    protected int count;

    @SerializedName("rating")
    protected String rating;

    @SerializedName("name")
    protected String name;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
