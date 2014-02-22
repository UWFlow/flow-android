package com.uwflow.flow_android.entities;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseFriend {
    private String name;
    private String status;

    public CourseFriend(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
