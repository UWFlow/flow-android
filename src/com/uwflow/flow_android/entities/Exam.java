package com.uwflow.flow_android.entities;

/**
 * Created by Chinmay on 3/1/14.
 */
public class Exam {
    private String first, second, third;

    public Exam(String first, String second, String third) {
        this.first = first;
        this.second = second;
        this.third = third;
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

    public String getThird() {
        return third;
    }

    public void setThird(String image) {
        this.third = image;
    }
}
