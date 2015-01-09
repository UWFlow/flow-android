package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * NOTE IF YOU CHANGE ANYTHING IN ANY OF THE DATABASE TABLE FILES, YOU MUST RE-RUN THE MAIN METHOD IN
 * DatabaseConfigUtil class in the util folder
 */

@DatabaseTable(tableName = "exam")
public class Exam implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("course_id")
    @DatabaseField (id = true)
    private String courseId;

    @DatabaseField
    private String url;

    @SerializedName("info_known")
    @DatabaseField
    private boolean infoKnown;

    @DatabaseField
    private String location;

    @SerializedName("location_known")
    @DatabaseField
    private boolean locationKnown;

    @DatabaseField
    private String sections;

    @SerializedName("start_date")
    @DatabaseField
    private long startDate;

    @SerializedName("end_date")
    @DatabaseField
    private long endDate;

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
}
