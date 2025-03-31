package com.sometodo.app.finance.Predict;

import com.sometodo.app.auth.entity.Account;
import com.sometodo.app.finance.Categories.Categories;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@ToString
@Setter
public class Predict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="year_value")
    private int year;
    @Column(name="month_value")
    private int month;
    @ManyToOne
    @JoinColumn(name = "category_id") //카테고리 아이디를 객체생성할때 넣으면 카테고리에 참조자를 만든다.
    private Categories category;
    @Column
    private int predict;
    @Column
    private int monthCost;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
}
