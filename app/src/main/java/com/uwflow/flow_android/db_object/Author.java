package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;

public class Author {
    @SerializedName("program_name")
    protected String programName;

    @SerializedName("profile_pic_url")
    protected String profilePicUrl;

    @SerializedName("id")
    protected String id;

    @SerializedName("name")
    protected String name;

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProfilePicUrl() {
	return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
	this.profilePicUrl = profilePicUrl;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}
