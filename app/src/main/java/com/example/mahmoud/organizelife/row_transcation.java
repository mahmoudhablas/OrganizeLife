package com.example.mahmoud.organizelife;

/**
 * Created by Mahmoud on 4/21/2018.
 */

public class row_transcation {
    private String subject;
    private double duration;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public row_transcation(String subject, double duration) {
        this.subject = subject;
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
