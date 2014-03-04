package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Overall implements Serializable{
    @SerializedName("count")
    protected int count;

    @SerializedName("rating")
    protected Double rating;

    public int getCount() {
	return count;
    }

    public void setCount(int count) {
	this.count = count;
    }

    public Double getRating() {
	return rating;
    }

    public void setRating(Double rating) {
	this.rating = rating;
    }
}
