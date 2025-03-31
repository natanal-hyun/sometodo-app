package com.example.todolist.finance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPay extends AppCompatActivity {
    Long categoriesId;
    boolean isSpend = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_new);
        Intent intent = getIntent();
        String date = intent.getStringExtra("Date");
        TextView editDate = findViewById(R.id.editDate);
        editDate.setText(date);
        EditText priceText = findViewById(R.id.priceText);
        EditText whereText = findViewById(R.id.whereText);
        Button back = findViewById(R.id.back);
        RadioGroup isSpendBox = findViewById(R.id.isSpendBox);

        Button moneyPost = findViewById(R.id.moneyPost);

        moneyPost.setOnClickListener(view -> {
            if (isSpendBox.getCheckedRadioButtonId()==-1){
                Toast.makeText(this,"지출인지, 수입인지 선택하시오!", Toast.LENGTH_SHORT).show();
            }else{
            String price = priceText.getText().toString();
            int intPrice = Integer.parseInt(price);
            String where = whereText.getText().toString();
            MoneyFlow createMoneyFlow = new MoneyFlow(null,categoriesId, date, where, intPrice, isSpend);
            Log.v("createMoneyFlow",createMoneyFlow.isSpend()+""); //트루리턴
            Call<MoneyFlow> call = FinanceActivity.moneyService.setMoneyFlow(categoriesId, createMoneyFlow);
            call.enqueue(new Callback<MoneyFlow>() {
                @Override
                public void onResponse(Call<MoneyFlow> call, Response<MoneyFlow> response) {
                    Intent intent = new Intent();
                    if (response.isSuccessful()) {
                        MoneyFlow moneyFlow = response.body();
                        Log.v("RETROFIT", "onResponse:성공, 결과 \n" + moneyFlow.toString()); //나옴
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            intent.putExtra("successfulDate", date);
                            Log.v("successfulDate", date + ""); //나옴
                            setResult(RESULT_OK, intent);
                            finish();
                        }, 1000);
//                        finish();  postDelayed가 실행되기 전에 액티비티가 꺼지는 경우. setResult엔 값이 없다. RESULT_OK 값을 전달하려면 핸들러 내부에서 전달해야함
                    }
                }

                @Override
                public void onFailure(Call<MoneyFlow> call, Throwable t) {
                }
            });
            }

        });

        back.setOnClickListener(view -> {
            finish();
        });


        isSpendBox.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.true_spend) isSpend = true;
            else isSpend=false;
            Log.v("isSpend",""+isSpend);
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                int selectedCateNum = pos + 1;
                categoriesId = Long.valueOf(selectedCateNum);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
