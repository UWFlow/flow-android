package com.uwflow.flow_android.util;

import android.widget.ExpandableListView;

import java.util.regex.Pattern;

/**
 * Created by david on 2014-03-08.
 */
public class CourseUtil {

    private static final Pattern courseIdSplitPattern = Pattern.compile("(?<=\\D)(?=\\d)");

    /**
     * Adds a space in the course ID between department (subject) and course code, and capitalizes.
     * Eg. "earth121l" => "Earth 121L"
     * @param courseId
     * @return Humanized course ID.
     */
    public static String humanizeCourseId(String courseId) {
        String[] parts = courseIdSplitPattern.split(courseId, 2);
        if (parts.length == 2) {
            return (parts[0] + " " + parts[1]).toUpperCase();
        } else {
            return courseId.toUpperCase();
        }
    }

    /**
     * Expands all groups in an ExpandableListView
     * @param listView ExpandableListView to expand
     */
    public static void expandAllGroups(ExpandableListView listView) {
        for (int i = 0; i < listView.getExpandableListAdapter().getGroupCount(); i++) {
            listView.expandGroup(i);
        }
    }
}
