package com.example.todolist.finance;

import static com.example.todolist.finance.FinanceActivity.formatting;
import static com.example.todolist.finance.FinanceActivity.moneyService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthPay extends AppCompatActivity {
    MonthPayAdapter monthPayAdapter;
    int year, month;
    Long categoryID;
    List<Categories> category;
    List<MoneyFlow> monthPayList;
    int monthPayment;

    public ActivityResultLauncher<Intent> getLauncher() {
        return launcher;
    }

    ActivityResultLauncher<Intent> launcher;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_predict_rv_act);
        Intent intent = getIntent();
        year = intent.getIntExtra("Year", 0);
        month = intent.getIntExtra("Month", 0);
        categoryID = intent.getLongExtra("categoryID", 0);
        PredictActivity.getCategories(() -> getMonthPay(year, month, categoryID)); // 순서중요 리사이클러뷰 나오고 그뒤에 나오면 리사이클러뷰에서 참조못함
        this.category = PredictActivity.category;

        RecyclerView monthUsRv = findViewById(R.id.month_pay_rv);
        monthUsRv.setLayoutManager(new LinearLayoutManager(this));
        monthPayAdapter = new MonthPayAdapter(this);
        monthUsRv.setAdapter(monthPayAdapter);

        for (Categories c : category) {
            Log.v("categoryId", c.toString());
        }
        String cName = category.get(categoryID.intValue() - 1).getCategoryName(); //get(0)부터가 1번리스트임 , 카테고리 아이디는 1번부터있다.
        TextView cNameText = findViewById(R.id.cName);
        cNameText.setText(cName);


        Button back = findViewById(R.id.back_m);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    intent.putExtra("successfulYear", year);
                    intent.putExtra("successfulMonth", month);
                    setResult(RESULT_OK, intent);
                    finish();
                }, 1000);
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        String strYear = data.getStringExtra("year");
                        String strMonth = data.getStringExtra("month");
                        int year = Integer.parseInt(strYear);
                        int month = Integer.parseInt(strMonth);
                        Long categoryId = data.getLongExtra("categoryID", 0);
                        Log.v("devideData", year + " " + month + " " + categoryId);
                        PredictActivity.getCategories(() -> getMonthPay(year, month, categoryID));
                        this.category = PredictActivity.category;
                    } else {
                        Log.d("ActivityResult", "RESULT_OK를 받지 못했습니다.");
                    }

                });
    }

    public void setMonthPayment() {
        monthPayment = 0;
        for (MoneyFlow m : monthPayList) {
            monthPayment += m.getCost();
            String monthPaymentText = formatting(monthPayment);
            TextView monthPayment = findViewById(R.id.monthPayment);
            monthPayment.setText(monthPaymentText);
        }
    }

    public void getMonthPay(int year, int month, Long categoryID) {
        Call<List<MoneyFlow>> call = moneyService.getMonthPay(categoryID, year, month);
        call.enqueue(new Callback<List<MoneyFlow>>() {
            @Override
            public void onResponse(Call<List<MoneyFlow>> call, Response<List<MoneyFlow>> response) {
                if (response.isSuccessful()) {
                    monthPayList = response.body();
                    setMonthPayment();
                    monthPayAdapter.setmData(category, monthPayList);
                    monthPayAdapter.notifyDataSetChanged();
                } else {
                    Log.v("RETROFIT", "onResponse: 실패");
                }
            }

            @Override
            public void onFailure(Call<List<MoneyFlow>> call, Throwable t) {
                Log.v("RETROFIT", "onResponse: " + t.getMessage());
            }
        });
    }
}
