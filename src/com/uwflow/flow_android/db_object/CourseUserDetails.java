package com.uwflow.flow_android.db_object;

import java.util.ArrayList;

public class CourseUserDetails {
    protected ArrayList<User> users;
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
