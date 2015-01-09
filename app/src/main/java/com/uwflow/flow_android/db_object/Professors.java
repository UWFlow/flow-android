package com.uwflow.flow_android.db_object;

import java.util.ArrayList;
import java.util.List;

public class Professors {
    protected List<Professor> professors = new ArrayList<Professor>();

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }
}
