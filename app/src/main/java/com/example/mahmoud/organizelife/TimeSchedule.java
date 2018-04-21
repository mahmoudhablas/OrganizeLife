package com.example.mahmoud.organizelife;
import java.util.*;
/**
 * Created by Mahmoud on 4/9/2018.
 */

public class TimeSchedule {
    private String subjectCategory;
    private Date date;
    private String note;
    private double duration;

    public TimeSchedule(String subjectCategory, String note,double duration) {
        this.subjectCategory = subjectCategory;
        this.note = note;
        this.duration = duration;
        date = new Date();
    }

    public String getSubjectCategory() {
        return subjectCategory;
    }

    public void setSubjectCategory(String subjectCategory) {
        this.subjectCategory = subjectCategory;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
