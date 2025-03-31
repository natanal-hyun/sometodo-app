package com.example.todolist.finance;

import static com.example.todolist.finance.FinanceActivity.formatting;
import static com.example.todolist.finance.FinanceActivity.moneyService;
import static com.example.todolist.finance.PredictActivity.category;
import static com.example.todolist.finance.PredictActivity.monthList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Budgeting extends AppCompatActivity {

    int selectYear;
    int selectMonth;
    int budgetSum;
    List<Predict> budgetList;
    List<Predict> updatedPredict;
    List<Categories> budgetCate;
    BudgetAdapter budgetAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_predict_budgetting);
        Intent intent_b = getIntent();
        String date = intent_b.getStringExtra("Date");
        budgetList = monthList;
        for (Predict p : budgetList) {
            budgetSum += p.getPredict();
        }
        TextView budgetSumText = findViewById(R.id.budgetSum);
        budgetSumText.setText("현재 예산은 "+formatting(budgetSum) + " 입니다.");

        String[] parts = date.split("-");
        selectYear = Integer.parseInt(parts[0]);
        selectMonth = Integer.parseInt(parts[1]);

        PredictActivity pa = new PredictActivity();
        budgetCate = category;

        TextView BudgetingMonth = findViewById(R.id.budgetingMonth);
        BudgetingMonth.setText(selectMonth + "월 예산");

        Button back = findViewById(R.id.back_b);
        back.setOnClickListener(v -> {
            finish();
        });

        RecyclerView budgetRv = findViewById(R.id.budget_rv);
        budgetRv.setLayoutManager(new LinearLayoutManager(this));
        budgetAdapter = new BudgetAdapter(this);
        budgetRv.setAdapter(budgetAdapter);

        budgetAdapter.setbData(budgetCate, budgetList);
        budgetAdapter.notifyDataSetChanged();

        Button fixBudget = findViewById(R.id.fixBudget);
        fixBudget.setOnClickListener(v -> {
            List<Predict> changedBudgetList = budgetAdapter.getbPredict();
            for (Predict p : changedBudgetList) {
                Log.v("budgetList", p.toString());
            }
            Call<List<Predict>> call=moneyService.budgetUpdate(changedBudgetList);
            call.enqueue(new Callback<List<Predict>>() {
                @Override
                public void onResponse(Call<List<Predict>> call, Response<List<Predict>> response) {
                    Intent intent=new Intent();
                    if (response.isSuccessful()) {
                        updatedPredict= response.body();
                        for(Predict p:updatedPredict) {
                            Log.v("RETROFIT", updatedPredict.toString());
                        }
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            intent.putExtra("successfulYear", selectYear);
                            intent.putExtra("successfulMonth",selectMonth);
                            setResult(RESULT_OK, intent);
                            finish();
                        }, 1000);
                    } else {
                        Log.v("RETROFIT", "onResponse: 실패");
                    }
                }

                @Override
                public void onFailure(Call<List<Predict>> call, Throwable t) {
                    Log.v("RETROFIT", "onResponse: " + t.getMessage());
                }
            });
        });
    }
}
