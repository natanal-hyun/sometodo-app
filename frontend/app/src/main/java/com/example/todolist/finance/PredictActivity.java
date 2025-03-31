package com.example.todolist.finance;

import static com.example.todolist.finance.FinanceActivity.formatting;
import static com.example.todolist.finance.FinanceActivity.moneyService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PredictActivity extends AppCompatActivity {
    int selectMonth, selectYear;
    String month;
    String year;
    String sendDate;
    TextView monthText;
    TextView monthCost;
    TextView monthPredict;
    TextView overSpend;
    PredictAdapter predictAdapter;

    static List<Predict> monthList = new ArrayList<>();
    static List<Categories> category = new ArrayList<>();

    ActivityResultLauncher<Intent> launcher;

    public ActivityResultLauncher<Intent> getLauncher() {
        return launcher;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_predict);
        Intent intent = getIntent();
        String date = intent.getStringExtra("Date");
        String[] parts = date.split("-");

        selectYear = Integer.parseInt(parts[0]);
        selectMonth = Integer.parseInt(parts[1]);
        month = selectYear + "년 " + selectMonth + "월";
        monthText = findViewById(R.id.monthText);
        monthText.setText(month);
        monthCost = findViewById(R.id.monthCost);
        monthPredict = findViewById(R.id.monthPredict);
        overSpend = findViewById(R.id.overSpending);
        getCategories(() -> showMonthCost(selectYear, selectMonth));
        sendDate = selectYear + "-" + selectMonth;


        Button back = findViewById(R.id.back_3);
        back.setOnClickListener(v -> {
            finish();
        });

        Button budgeting = findViewById(R.id.budgeting);
        budgeting.setOnClickListener(v -> {
            if (!monthList.isEmpty()) {
                Intent intent_b = new Intent(PredictActivity.this, Budgeting.class);
                intent_b.putExtra("Date", sendDate);
                launcher.launch(intent_b);
            } else {
                Toast.makeText(this, "이달 사용건이 1건 이상이어야 합니다", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView predictRv = findViewById(R.id.predictRv);
        predictRv.setLayoutManager(new LinearLayoutManager(this));
        predictAdapter = new PredictAdapter(this);
        predictRv.setAdapter(predictAdapter);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        int getYear = data.getIntExtra("successfulYear", 0);
                        int getMonth = data.getIntExtra("successfulMonth", 0);

                        getCategories(() ->showMonthCost(getYear, getMonth)); //showMonthCost 안에서 뷰가 실행되니까 뷰에서 카테고리를 쓰려면 미리 불러와져있어야한다.

                    } else {
                        Log.d("ActivityResult", "RESULT_OK를 받지 못했습니다.");
                    }

                });
    }

    static public void getCategories(OnCategoriesLoadedListener listener) {
        Call<List<Categories>> call = moneyService.getCategories();
        call.enqueue(new Callback<List<Categories>>() {

            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
                if (response.isSuccessful()) {
                    category = response.body();
                    listener.onSuccess();
                } else {
                    Log.v("RETROFIT", "onResponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<List<Categories>> call, Throwable t) {
                Log.v("RETROFIT", "onResponse: " + t.getMessage());
            }
        });
    }
    interface OnCategoriesLoadedListener{
        void onSuccess();
    }

    public void showMonthCost(int selectYear, int selectMonth) {
        Call<List<Predict>> monthCostByCategory = moneyService.getMonthlyCost(selectYear, selectMonth);
        monthCostByCategory.enqueue(new Callback<List<Predict>>() {

            @Override
            public void onResponse(Call<List<Predict>> call, Response<List<Predict>> response) {
                int totalCost = 0;
                int totalPredict = 0;

                if (response.isSuccessful() && response.body() != null) {
                    monthList = response.body();
                    for (Predict m : monthList) {
                        totalCost += m.getMonthCost();
                        totalPredict += m.getPredict();
                        Log.v("monthList", m.toString());
                    }
                    monthCost.setText(formatting(totalCost) + "");
                    monthPredict.setText(formatting(totalPredict) + "");
                    int isOver = totalPredict - totalCost;
                    if (isOver >= 0)
                        overSpend.setText("총 예산       " + formatting(isOver) + "       남음");
                    else overSpend.setText(formatting(Math.abs(isOver)) + "        과소비");
                    predictAdapter.setmData(category, monthList);
                    predictAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onFailure(Call<List<Predict>> call, Throwable t) {
                Toast.makeText(PredictActivity.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void moveMonth(View view) {
        if (view.getId() == R.id.plusMonth) {
            if (selectMonth < 12)
                selectMonth += 1;
            else {
                Toast.makeText(this, "12월을 초과할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if (selectMonth > 1)
                selectMonth -= 1;
            else {
                Toast.makeText(this, "1월 미만일 수 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        String month = selectYear + "년 " + selectMonth + "월";
        monthText.setText(month);
        showMonthCost(selectYear, selectMonth);
        sendDate = selectYear + "-" + selectMonth;
    }
}
