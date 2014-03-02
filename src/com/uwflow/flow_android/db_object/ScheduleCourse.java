package com.uwflow.flow_android.db_object;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

@DatabaseTable(tableName = "schedule_course")
public class ScheduleCourse implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String profId;

    @DatabaseField
    private String courseId;

    @DatabaseField (dataType = DataType.DATE)
    private Date startDate;

    @DatabaseField (dataType = DataType.DATE)
    private Date endDate;

    @DatabaseField
    private String building;

    @DatabaseField
    private String room;

    @DatabaseField
    private String termId;

    @DatabaseField
    private String sectionType;

    @DatabaseField
    private String sectionNum;

    @DatabaseField
    private String classNum;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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
}
