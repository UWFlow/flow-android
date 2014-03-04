package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Meeting {
    @SerializedName("building")
    protected String building;

    @SerializedName("is_tba")
    protected boolean isTba;

    @SerializedName("room")
    protected String room;

    @SerializedName("end_date")
    protected long endDate;

    @SerializedName("days")
    protected ArrayList<String> days;

    @SerializedName("start_seconds")
    protected long startSeconds;

    @SerializedName("prof_id")
    protected String profId;

    @SerializedName("end_seconds")
    protected long endSeconds;

    @SerializedName("is_cancelled")
    protected boolean isCancelled;

    @SerializedName("start_date")
    protected long startDate;

    @SerializedName("is_closed")
    protected boolean isClosed;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public boolean isTba() {
        return isTba;
    }

    public void setTba(boolean isTba) {
        this.isTba = isTba;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public long getStartSeconds() {
        return startSeconds;
    }

    public void setStartSeconds(long startSeconds) {
        this.startSeconds = startSeconds;
    }

    public String getProfId() {
        return profId;
    }

    public void setProfId(String profId) {
        this.profId = profId;
    }

    public long getEndSeconds() {
        return endSeconds;
    }

    public void setEndSeconds(long endSeconds) {
        this.endSeconds = endSeconds;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }
}
