package com.uwflow.flow_android.entities;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by jasperfung on 2/22/14.
 */
public class CourseClass {
    private String sectionType;
    private Integer sectionNum;

    private String professor;

    private Integer enrollmentTotal;
    private Integer enrollmentCapacity;

    private Integer startTimeSeconds;
    private Integer endTimeSeconds;
    private ArrayList<String> days;

    private String building;
    private String room;
    private String campus;

    private Boolean subscribed;

    public CourseClass(String sectionType,
                       Integer sectionNum,
                       String professor,
                       Integer enrollmentTotal,
                       Integer enrollmentCapacity,
                       Integer startTimeSeconds,
                       Integer endTimeSeconds,
                       ArrayList<String> days,
                       String building,
                       String room,
                       String campus,
                       Boolean subscribed) {
        this.sectionType = sectionType;
        this.sectionNum = sectionNum;
        this.professor = professor;
        this.enrollmentTotal = enrollmentTotal;
        this.enrollmentCapacity = enrollmentCapacity;
        this.startTimeSeconds = startTimeSeconds;
        this.endTimeSeconds = endTimeSeconds;
        this.days = days;
        this.building = building;
        this.room = room;
        this.campus = campus;
        this.subscribed = subscribed;
    }

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public Integer getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(Integer sectionNum) {
        this.sectionNum = sectionNum;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public Integer getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public void setEnrollmentTotal(Integer enrollmentTotal) {
        this.enrollmentTotal = enrollmentTotal;
    }

    public Integer getEnrollmentCapacity() {
        return enrollmentCapacity;
    }

    public void setEnrollmentCapacity(Integer enrollmentCapacity) {
        this.enrollmentCapacity = enrollmentCapacity;
    }

    public Integer getStartTimeSeconds() {
        return startTimeSeconds;
    }

    public void setStartTimeSeconds(Integer startTimeSeconds) {
        this.startTimeSeconds = startTimeSeconds;
    }

    public Integer getEndTimeSeconds() {
        return endTimeSeconds;
    }

    public void setEndTimeSeconds(Integer endTimeSeconds) {
        this.endTimeSeconds = endTimeSeconds;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
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

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }
}
