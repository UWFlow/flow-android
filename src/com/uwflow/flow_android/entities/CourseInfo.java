package com.uwflow.flow_android.entities;

import java.util.ArrayList;

/**
 * Created by jasperfung on 3/2/14.
 */
public class CourseInfo {
    private String id, code, name, description;
    CourseOverallRating overallRating;
    ArrayList<CourseReview> reviews;

    public CourseInfo(String id, String code, String name, String description, CourseOverallRating overallRating, ArrayList<CourseReview> reviews) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.overallRating = overallRating;
        this.reviews = reviews;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CourseOverallRating getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(CourseOverallRating overallRating) {
        this.overallRating = overallRating;
    }

    public ArrayList<CourseReview> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<CourseReview> reviews) {
        this.reviews = reviews;
    }

    public boolean isEmpty() {
        // TODO: maybe we want to change these &&s to ||s
        return id.isEmpty() && code.isEmpty() && name.isEmpty() && description.isEmpty() && reviews.isEmpty();
    }
}
