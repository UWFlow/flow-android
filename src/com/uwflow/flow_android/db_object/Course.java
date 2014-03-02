package com.uwflow.flow_android.db_object;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

@DatabaseTable(tableName = "course")
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField (id = true)
    private String userCourseId;

    @DatabaseField
    private String id;

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private ArrayList<Rating> ratings;

    @DatabaseField
    private String code;

    @DatabaseField
    private String name;

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private ArrayList<String> professorIds;

    @DatabaseField
    private String prereqs;

    @DatabaseField
    private int overallCount;

    @DatabaseField
    private Double overallRating;

    @DatabaseField
    private String description;


    public Course(){

    }

    public Course(String description, String id, ArrayList<Rating> ratings, String userCourseId, String code, String name, ArrayList<String> professorIds, String prereqs, int overallCount, double overallRating) {
        this.description = description;
        this.id = id;
        this.ratings = ratings;
        this.userCourseId = userCourseId;
        this.code = code;
        this.name = name;
        this.professorIds = professorIds;
        this.prereqs = prereqs;
        this.overallCount = overallCount;
        this.overallRating = overallRating;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<Rating> ratings) {
        this.ratings = ratings;
    }

    public String getUserCourseId() {
        return userCourseId;
    }

    public void setUserCourseId(String userCourseId) {
        this.userCourseId = userCourseId;
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

    public ArrayList<String> getProfessorIds() {
        return professorIds;
    }

    public void setProfessorIds(ArrayList<String> professorIds) {
        this.professorIds = professorIds;
    }

    public String getPrereqs() {
        return prereqs;
    }

    public void setPrereqs(String prereqs) {
        this.prereqs = prereqs;
    }

    public int getOverallCount() {
        return overallCount;
    }

    public void setOverallCount(int overallCount) {
        this.overallCount = overallCount;
    }

    public double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
