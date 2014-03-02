package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TermUser {
    @SerializedName("term_id")
    protected String termId;

    @SerializedName("user_ids")
    protected List<String> userIds;

    @SerializedName("term_name")
    protected String termName;

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }
}
