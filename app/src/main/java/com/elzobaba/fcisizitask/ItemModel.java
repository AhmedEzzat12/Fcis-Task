package com.elzobaba.fcisizitask;

/**
 * Created by ahmed on 3/19/2017.
 */

public class ItemModel {
    private String Assignment_Name;
    private String Course;
    private String Deadline;

    public ItemModel(String assignment_Name, String course, String deadline) {
        Assignment_Name = assignment_Name;
        Course = course;
        Deadline = deadline;
    }

    public String getAssignment_Name() {
        return Assignment_Name;
    }

    public void setAssignment_Name(String assignment_Name) {
        Assignment_Name = assignment_Name;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getDeadline() {
        return Deadline;
    }

    public void setDeadline(String deadline) {
        Deadline = deadline;
    }
}
