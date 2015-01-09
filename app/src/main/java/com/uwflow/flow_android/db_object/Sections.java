package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Sections {
    @SerializedName("sections")
    protected List<Section> sections;

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
