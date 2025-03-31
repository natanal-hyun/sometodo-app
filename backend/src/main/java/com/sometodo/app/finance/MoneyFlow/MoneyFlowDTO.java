package com.sometodo.app.finance.MoneyFlow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Getter
public class MoneyFlowDTO {
    private Long id;
    private LocalDate nowDate;

    private Long categoriesId;
    private String content;
    private int cost;
    private boolean spend;

    public static MoneyFlowDTO createMoneyFlowDTO(MoneyFlow moneyFlow){
        return new MoneyFlowDTO(
                moneyFlow.getId(),
                moneyFlow.getNowDate(),
                moneyFlow.getCategory().getId(),
                moneyFlow.getContent(),
                moneyFlow.getCost(),
                moneyFlow.isSpend()
        );
    }

}
