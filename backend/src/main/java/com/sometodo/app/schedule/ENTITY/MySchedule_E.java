package com.sometodo.app.schedule.ENTITY;

import com.sometodo.app.auth.entity.Account;
import com.sometodo.app.schedule.DTO.MySchedule_D;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class MySchedule_E {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schedule_id;

    @Column
    private String content;

    @Column
    private String category;

    @Column
    private int year;

    @Column
    private int month;

    @Column
    private int day;

    @Column
    private boolean schedule_isAchievement;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public static MySchedule_E dtoToEntity(MySchedule_D dto, Account account){
        MySchedule_E entity = new MySchedule_E(dto.getSchedule_id(),
                dto.getContent(),
                dto.getCategory(),
                dto.getYear(),
                dto.getMonth(),
                dto.getDay(),
                dto.isSchedule_isAchievement(),
                account);
        return entity;
    }

}
