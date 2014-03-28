package com.uwflow.flow_android.entities;

/**
 * Created by jasperfung on 2/28/14.
 */
public class NavDrawerItem {
    private int id;
    private String name;
    private int iconResID;

    public NavDrawerItem(int id, String name, int iconResID) {
        this.id = id;
        this.name = name;
        this.iconResID = iconResID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
