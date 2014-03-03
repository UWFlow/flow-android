package com.uwflow.flow_android.db_object;

import java.util.ArrayList;
import java.util.List;

public class Exams {
    protected List<Exam> exams = new ArrayList<Exam>();

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }
}
