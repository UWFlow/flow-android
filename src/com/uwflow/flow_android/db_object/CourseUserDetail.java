package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CourseUserDetail {
    @SerializedName("users")
    protected ArrayList<User> users;

    @SerializedName("term_users")
    protected ArrayList<TermUser> termUsers;

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<TermUser> getTermUsers() {
        return termUsers;
    }

    public void setTermUsers(ArrayList<TermUser> termUsers) {
        this.termUsers = termUsers;
    }
}
