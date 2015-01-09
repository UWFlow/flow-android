package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Exams {
    protected List<Exam> exams = new ArrayList<Exam>();

    @SerializedName("last_updated_date")
    protected Timestamp lastUpdatedTimestamp;

    public Timestamp getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(Timestamp lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }
}
