package it.uniroma2.dicii.ispw.sostudy.bean;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;


public class TestBean {
    private String name;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private Duration duration;
    private int maxScore;
    private String virtualClass;

    public TestBean(String name, LocalDate dueDate, LocalTime dueTime, Duration duration, String virtualClass) {
        this.name = name;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.duration = duration;
        //this.maxScore = maxScore;
        this.virtualClass = virtualClass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getName() {
        return name;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public String getVirtualClass() {
        return virtualClass;
    }

    public void setVirtualClass(String virtualClass) {
        this.virtualClass = virtualClass;
    }

    public void setDueTime(LocalTime dueTime) {
        this.dueTime = dueTime;
    }

    public LocalTime getDueTime() {
        return dueTime;
    }
}
