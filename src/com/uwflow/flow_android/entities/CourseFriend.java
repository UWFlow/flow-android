package com.uwflow.flow_android.entities;

import android.graphics.Bitmap;
import com.uwflow.flow_android.db_object.User;

/**
 * Created by jasperfung on 2/21/14.
 */
public class CourseFriend {
    private String termName;  // term that the user took this course
    private User user;

    public CourseFriend(String termName, User user) {
        this.termName = termName;
        this.user = user;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
