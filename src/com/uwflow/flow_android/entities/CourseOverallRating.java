package com.uwflow.flow_android.entities;

/**
 * Created by jasperfung on 3/2/14.
 */
public class CourseOverallRating {
    int overallCount, usefulCount, easyCount;
    double overallRating, usefulRating, easyRating;

    public CourseOverallRating(int overallCount, double overallRating, int usefulCount, double usefulRating, int easyCount, double easyRating) {
        this.overallCount = overallCount;
        this.overallRating = overallRating;
        this.usefulCount = usefulCount;
        this.usefulRating = usefulRating;
        this.easyCount = easyCount;
        this.easyRating = easyRating;
    }

    public int getOverallCount() {
        return overallCount;
    }

    public void setOverallCount(int overallCount) {
        this.overallCount = overallCount;
    }

    public double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(double overallRating) {
        this.overallRating = overallRating;
    }

    public int getUsefulCount() {
        return usefulCount;
    }

    public void setUsefulCount(int usefulCount) {
        this.usefulCount = usefulCount;
    }

    public double getUsefulRating() {
        return usefulRating;
    }

    public void setUsefulRating(double usefulRating) {
        this.usefulRating = usefulRating;
    }

    public int getEasyCount() {
        return easyCount;
    }

    public void setEasyCount(int easyCount) {
        this.easyCount = easyCount;
    }

    public double getEasyRating() {
        return easyRating;
    }

    public void setEasyRating(double easyRating) {
        this.easyRating = easyRating;
    }
}
