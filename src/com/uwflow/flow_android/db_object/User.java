package com.uwflow.flow_android.db_object;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String IS_ME = "is_me";

    @DatabaseField(id = true)
    private String id;

    @DatabaseField(canBeNull = false)
    private String firstName;

    @DatabaseField(canBeNull = false)
    private int point;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] images = new String[3];

    @DatabaseField(canBeNull = false)
    private String programName;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String lastName;

    @DatabaseField(canBeNull = false)
    private int numInvites;

    @DatabaseField(canBeNull = false)
    private long fbid;

    @DatabaseField(canBeNull = false, columnName = "is_me")
    private boolean isMe;

    public User() {
    }

    public User(String id, String firstName, int point, String[] images, String programName, String name, String lastName, int numInvites, long fbid, boolean isMe) {
        this.id = id;
        this.firstName = firstName;
        this.point = point;
        this.images = images;
        this.programName = programName;
        this.name = name;
        this.lastName = lastName;
        this.numInvites = numInvites;
        this.fbid = fbid;
        this.isMe = isMe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getNumInvites() {
        return numInvites;
    }

    public void setNumInvites(int numInvites) {
        this.numInvites = numInvites;
    }

    public long getFbid() {
        return fbid;
    }

    public void setFbid(long fbid) {
        this.fbid = fbid;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }
}
