package com.uwflow.flow_android.db_object;

import java.util.ArrayList;
import java.util.List;

public class Users {
    protected List<User> users = new ArrayList<User>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
