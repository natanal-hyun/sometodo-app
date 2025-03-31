package com.sometodo.app.schedule.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class MySchedule_D {
    private Long schedule_id;
    private String content;
    private String category;
    private int year;
    private int month;
    private int day;
    private boolean schedule_isAchievement;
}
