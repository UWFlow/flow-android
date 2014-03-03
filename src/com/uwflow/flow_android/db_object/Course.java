package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

@DatabaseTable(tableName = "course")
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("user_course_id")
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

    @SerializedName("professor_ids")
    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private ArrayList<String> professorIds;

    @DatabaseField
    private String prereqs;

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    private Overall overall;

    @DatabaseField
    private String description;

    public Course(){
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

    public void setPrereqs(String prereqs) {
        this.prereqs = prereqs;
    }

    public Overall getOverall() {
        return overall;
    }

    public void setOverall(Overall overall) {
        this.overall = overall;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
