package com.uwflow.flow_android.db_object;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;


@DatabaseTable(tableName = "exam")
public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField (id = true)
    private String courseId;

    @DatabaseField
    private String url;

    @DatabaseField
    private boolean infoKnown;

    @DatabaseField
    private String location;

    @DatabaseField
    private boolean locationKnown;

    @DatabaseField
    private String sections;

    @DatabaseField(dataType = DataType.DATE)
    private Date startDate;

    @DatabaseField(dataType = DataType.DATE)
    private Date endDate;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isInfoKnown() {
        return infoKnown;
    }

    public void setInfoKnown(boolean infoKnown) {
        this.infoKnown = infoKnown;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public boolean isLocationKnown() {
        return locationKnown;
    }

    public void setLocationKnown(boolean locationKnown) {
        this.locationKnown = locationKnown;
    }

    public String getSections() {
        return sections;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
