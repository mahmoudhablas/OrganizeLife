package com.example.mahmoud.organizelife;

/**
 * Created by Mahmoud on 4/21/2018.
 */

public class row_transcation {
    int id;
    private String subject;
    private double duration;
    private String type;
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public row_transcation(int id, String subject, double duration, String note) {
        this.id = id;
        this.subject = subject;
        this.duration = duration;
        this.note = note;
    }
    public row_transcation(int id, String transaction, double amount, String note,String type) {
        this.id = id;
        this.subject = transaction;
        this.duration = amount;
        this.note = note;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public row_transcation(String subject, double duration,int id) {
        this.subject = subject;
        this.duration = duration;
        this.type = "null";
        this.id = id;
    }
    public row_transcation(String subject, double duration,String type,int id)
    {
        this.subject = subject;
        this.duration = duration;
        this.type = type;
        this.id = id;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
