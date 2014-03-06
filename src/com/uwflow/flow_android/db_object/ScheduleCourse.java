package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "schedule_course")
public class ScheduleCourse implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true)
    private String id;

    @SerializedName("prof_id")
    @DatabaseField
    private String profId;


    @SerializedName("course_id")
    @DatabaseField
    private String courseId;

    @SerializedName("start_date")
    @DatabaseField
    private long startDate;

    @SerializedName("end_date")
    @DatabaseField
    private long endDate;

    @DatabaseField
    private String building;

    @DatabaseField
    private String room;

    @SerializedName("term_id")
    @DatabaseField
    private String termId;

    @SerializedName("section_type")
    @DatabaseField
    private String sectionType;

    @SerializedName("section_num")
    @DatabaseField
    private String sectionNum;

    @SerializedName("class_num")
    @DatabaseField
    private String classNum;

    @DatabaseField
    private String scheduleUrl;

    public ScheduleCourse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfId() {
        return profId;
    }

    public void setProfId(String profId) {
        this.profId = profId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public String getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(String sectionNum) {
        this.sectionNum = sectionNum;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public String getScheduleUrl() {
        return scheduleUrl;
    }

    public void setScheduleUrl(String scheduleUrl) {
        this.scheduleUrl = scheduleUrl;
    }
}
