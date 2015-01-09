package com.uwflow.flow_android.db_object;

import java.util.ArrayList;

public class UserFriends {
    protected ArrayList<User> friends = new ArrayList<User>();

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }
}
