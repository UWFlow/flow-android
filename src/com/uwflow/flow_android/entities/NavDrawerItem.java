package com.uwflow.flow_android.entities;

/**
 * Created by jasperfung on 2/28/14.
 */
public class NavDrawerItem {
    private String name;
    private int iconResID;

    public NavDrawerItem(String name, int iconResID) {
        this.name = name;
        this.iconResID = iconResID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconResID() {
        return iconResID;
    }

    public void setIconResID(int iconResID) {
        this.iconResID = iconResID;
    }
}
