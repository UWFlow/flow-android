package com.uwflow.flow_android.db_object;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CourseDetail {

    protected String code;
    protected String name;
    protected Overall overall;
    protected String prereqs;
    protected String id;
    protected String description;
    protected List<Review> reviews;
    @SerializedName("professor_ids")
    protected ArrayList<String> professorIds;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<String> getProfessorIds() {
        return professorIds;
    }

    public void setProfessorIds(ArrayList<String> professorIds) {
        this.professorIds = professorIds;
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

    public Overall getOverall() {
        return overall;
    }

    public void setOverall(Overall overall) {
        this.overall = overall;
    }

    public String getPrereqs() {
        return prereqs;
    }

    public void setPrereqs(String prereqs) {
        this.prereqs = prereqs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
