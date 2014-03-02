package com.uwflow.flow_android.db_object;

import java.io.Serializable;

public class Rating implements Serializable {
    private static final long serialVersionUID = 1L;

    private int count;
    private double rating;
    private String name;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
