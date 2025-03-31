package com.sometodo.app.schedule.ENTITY;

import com.sometodo.app.auth.entity.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MyToDo_E {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todo_id;

    @Column
    private String todo_content;
    @Column
    private boolean todo_isAchievement;
    @Column
    private int importance;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;


}
