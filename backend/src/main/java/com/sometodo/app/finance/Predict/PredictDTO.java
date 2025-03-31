package com.sometodo.app.finance.Predict;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PredictDTO {
    private Long id;
    private int year;
    private int month;
    private Long categoryId;
    private int predict;
    private int monthCost;

    public static PredictDTO createPredictDTO(Predict predict) {
        return new PredictDTO(
                predict.getId(),
                predict.getYear(),
                predict.getMonth(),
                predict.getCategory().getId(),
                predict.getPredict(),
                predict.getMonthCost()
        );
    }
}
