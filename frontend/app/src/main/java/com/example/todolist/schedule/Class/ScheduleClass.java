package com.example.todolist.schedule.Class;

import java.io.Serializable;

public class ScheduleClass implements Serializable {
    private Long schedule_id;
    private String content;
    private String category;
    private int year;
    private int month;
    private int day;
    private boolean schedule_isAchievement;

    //CONSTRUCTOR
    public ScheduleClass(String content, String category, int year, int month, int day, Long schedule_id, boolean schedule_isAchievement) {
        this.content = content;
        this.category = category;
        this.year = year;
        this.month = month;
        this.day = day;
        this.schedule_id = schedule_id;
        this.schedule_isAchievement = schedule_isAchievement;
    }


    //GETTER
    public String getContent() {
        return content;
    }
    public String getCategory() {
        return category;
    }
    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }
    public int getDay() {
        return day;
    }
    public Long getId() {
        return schedule_id;
    }
    public boolean isAchievement() {
        return schedule_isAchievement;
    }


//    SETTER
//    public void setContent(String content) {
//        this.content = content;
//    }
//    public void setCategory(String category) {
//        this.category = category;
//    }
//    public void setYear(int year) {
//        this.year = year;
//    }
//    public void setMonth(int month) {
//        this.month = month;
//    }
//    public void setDay(int day) {
//        this.day = day;
//    }
//
//    public void setSchedule_isAchievement(boolean schedule_isAchievement) {
//        this.schedule_isAchievement = schedule_isAchievement;
//    }

    @Override
    public String toString() {
        return "ScheduleClass{" +
                "content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", schedule_id=" + schedule_id +
                ", schedule_isAchievement=" + schedule_isAchievement +
                '}';
    }
}
