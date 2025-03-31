package com.sometodo.app.schedule.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Setter
public class MyToDo_D {
    private String todo_content;
    private boolean todo_isAchievement;
    private int importance;  //0~10
    private Long todo_id;
}
