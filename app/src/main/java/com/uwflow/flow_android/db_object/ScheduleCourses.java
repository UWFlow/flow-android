package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ScheduleCourses {
    @SerializedName("screenshot_url")
    protected String screenshotUrl;

    @SerializedName("schedule")
    protected ArrayList<ScheduleCourse> scheduleCourses = new ArrayList<ScheduleCourse>();

    public ArrayList<ScheduleCourse> getScheduleCourses() {
        return scheduleCourses;
    }

    public void setScheduleCourses(ArrayList<ScheduleCourse> scheduleCourses) {
        this.scheduleCourses = scheduleCourses;
    }

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }
}
