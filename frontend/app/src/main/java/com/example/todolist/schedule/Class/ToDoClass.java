package com.example.todolist.schedule.Class;

import java.io.Serializable;

public class ToDoClass implements Serializable {
    private String todo_content;
    private boolean todo_isAchievement;
    private int importance;
    private Long todo_id;


    //CONSTRUCTOR
    public ToDoClass(String todo_content, boolean todo_isAchievement, int importance, Long todo_id) {
        this.todo_content = todo_content;
        this.todo_isAchievement = todo_isAchievement;
        this.importance = importance;
        this.todo_id = todo_id;
    }


    //GETTER
    public String getTodo_content() {
        return todo_content;
    }
    public boolean isAchievement() {
        return todo_isAchievement;
    }
    public int getImportance() {
        return importance;
    }
    public Long getTodo_id() {
        return todo_id;
    }


    //SETTER
    public void setTodo_content(String todo_content) {
        this.todo_content = todo_content;
    }
    public void setAchievement(boolean achievement) {
        this.todo_isAchievement = achievement;
    }
    public void setImportance(int importance) {
        this.importance = importance;
    }
    public void setId(Long id) {
        this.todo_id = id;
    }

    @Override
    public String toString() {
        return "ToDoClass{" +
                "todo_content='" + todo_content + '\'' +
                ", achievement=" + todo_isAchievement +
                ", importance=" + importance +
                ", id=" + todo_id +
                '}';
    }
}
