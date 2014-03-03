package com.uwflow.flow_android.db_object;

import java.util.ArrayList;
import java.util.List;

public class TermUser {
    protected String termId;
    protected List<String> userIds;
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
