package com.uwflow.flow_android.db_object;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * NOTE IF YOU CHANGE ANYTHING IN ANY OF THE DATABASE TABLE FILES, YOU MUST RE-RUN THE MAIN METHOD IN
 * DatabaseConfigUtil class in the util folder
 */
@DatabaseTable(tableName = "user_course")
public class UserCourse implements Serializable {

    @DatabaseField(id = true)
    protected String id;

    @DatabaseField
    @SerializedName("user_id")
    protected String userId;

    @DatabaseField
    @SerializedName("term_name")
    protected String termName;

    @DatabaseField
    @SerializedName("term_id")
    protected String termId;

    @DatabaseField
    @SerializedName("has_reviewed")
    protected String hasReviewed;

    @DatabaseField
    @SerializedName("professor_id")
    protected String professorId;

    @DatabaseField
    @SerializedName("course_id")
    protected String courseId;

    @DatabaseField
    @SerializedName("program_year_id")
    protected String programYearId;

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    @SerializedName("professor_review")
    protected Review professorReview;

    @DatabaseField (dataType = DataType.SERIALIZABLE)
    @SerializedName("course_review")
    protected Review courseReview;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public String getHasReviewed() {
        return hasReviewed;
    }

    public void setHasReviewed(String hasReviewed) {
        this.hasReviewed = hasReviewed;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getProgramYearId() {
        return programYearId;
    }

    public void setProgramYearId(String programYearId) {
        this.programYearId = programYearId;
    }

    public Review getProfessorReview() {
        return professorReview;
    }

    public void setProfessorReview(Review professorReview) {
        this.professorReview = professorReview;
    }

    public Review getCourseReview() {
        return courseReview;
    }

    public void setCourseReview(Review courseReview) {
        this.courseReview = courseReview;
    }
}