package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CourseDetail {
    @SerializedName("ratings")
    protected List<Rating> ratings;

    @SerializedName("code")
    protected String code;

    @SerializedName("name")
    protected String name;

    @SerializedName("overall")
    protected Overall overall;

    @SerializedName("reviews")
    protected List<Review> reviews;

    @SerializedName("professor_ids")
    protected ArrayList<String> professorIds;

    @SerializedName("prereqs")
    protected String prereqs;

    @SerializedName("id")
    protected String id;

    @SerializedName("description")
    protected String description;

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<String> getProfessorIds() {
        return professorIds;
    }

    public void setProfessorIds(ArrayList<String> professorIds) {
        this.professorIds = professorIds;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Overall getOverall() {
        return overall;
    }

    public void setOverall(Overall overall) {
        this.overall = overall;
    }

    public String getPrereqs() {
        return prereqs;
    }

    public void setPrereqs(String prereqs) {
        this.prereqs = prereqs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
