package com.uwflow.flow_android.entities;

/**
 * Created by Chinmay on 3/1/14.
 */
public class Course {
    private String first, second;

    public Course(String first, String second) {
        this.first = first;
        this.second = second;
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
}
