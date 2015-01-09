package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Section {
    @SerializedName("note")
    protected String note;

    @SerializedName("units")
    protected Double units;

    @SerializedName("last_updated")
    protected Timestamp lastUpdated;

    @SerializedName("class_num")
    protected String classNum;

    @SerializedName("term_id")
    protected String termId;

    @SerializedName("meetings")
    protected ArrayList<Meeting> meetings;

    @SerializedName("section_type")
    protected String sectionType;

    @SerializedName("campus")
    protected String campus;

    @SerializedName("waiting_total")
    protected int waitingTotal;

    @SerializedName("enrollment_capacity")
    protected int enrollmentCapacity;

    @SerializedName("section_num")
    protected String sectionNum;

    @SerializedName("enrollment_total")
    protected int enrollmentTotal;

    @SerializedName("course_id")
    protected String courseId;

    @SerializedName("waiting_capacity")
    protected int waitingCapacity;

    @SerializedName("id")
    protected String id;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getUnits() {
        return units;
    }

    public void setUnits(Double units) {
        this.units = units;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public ArrayList<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(ArrayList<Meeting> meetings) {
        this.meetings = meetings;
    }

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public int getWaitingTotal() {
        return waitingTotal;
    }

    public void setWaitingTotal(int waitingTotal) {
        this.waitingTotal = waitingTotal;
    }

    public int getEnrollmentCapacity() {
        return enrollmentCapacity;
    }

    public void setEnrollmentCapacity(int enrollmentCapacity) {
        this.enrollmentCapacity = enrollmentCapacity;
    }

    public String getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(String sectionNum) {
        this.sectionNum = sectionNum;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public void setEnrollmentTotal(int enrollmentTotal) {
        this.enrollmentTotal = enrollmentTotal;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getWaitingCapacity() {
        return waitingCapacity;
    }

    public void setWaitingCapacity(int waitingCapacity) {
        this.waitingCapacity = waitingCapacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
