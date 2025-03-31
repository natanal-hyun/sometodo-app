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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPay extends AppCompatActivity {
    MoneyFlow moneyFlow;
    Long categoriesId;
    boolean isSpend=false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_edit);
        Intent intent=getIntent();
        int pos=intent.getIntExtra("position",0);
        String fromActivity=intent.getStringExtra("fromActivity");
        Log.v("MainActivity",fromActivity);
        if(("MainActivity").equals(fromActivity)){
            Log.v("MainActivity","hello");
            moneyFlow=DayAdapter.moneyFlowList.get(pos);
        }
        else{
            Log.v("MainActivity12","world");
            moneyFlow=MonthPayAdapter.mList.get(pos);
        }
        Log.v("MoneyFlowListPos",""+pos);

        Button back = findViewById(R.id.back_2);
        back.setOnClickListener(view -> {
            finish();
        });
        String cost= moneyFlow.getCost()+"";
        String content= moneyFlow.getContent();

        //xml에서 부르고 ->원래 참조자값 써주고-> 새로쓴값 변수만들고 -> 새 참조자 만들고
        EditText wehereText_e=findViewById(R.id.whereText_e);
        EditText priceText_e=findViewById(R.id.priceText_e);
        TextView editDate=findViewById(R.id.editDate);
        String date=moneyFlow.getNowDate();
        editDate.setText(date);

        wehereText_e.setText(content);   //불러오기
        priceText_e.setText(cost);
        RadioGroup spendBox=findViewById(R.id.spendBox);
        RadioButton trueSpend=findViewById(R.id.true_spend);
        RadioButton falseSepnd=findViewById(R.id.false_spend);

        spendBox.setOnCheckedChangeListener((radioGroup, i) -> {
            if(i==R.id.true_spend) isSpend=true;
            else isSpend=false;
        });

        Button moneyPatch=findViewById(R.id.moneyPatch);

        moneyPatch.setOnClickListener(v->{
            String where = wehereText_e.getText().toString(); // 새로쓴거 서버 보낼준비
            int intPrice = Integer.parseInt(priceText_e.getText().toString());
            MoneyFlow patchMoneyFlow = new MoneyFlow(moneyFlow.getId(),categoriesId, moneyFlow.getNowDate(), where, intPrice, isSpend);
            Log.v("patchMoneyFlow",patchMoneyFlow.toString()+"");
            Call<MoneyFlow> call = FinanceActivity.moneyService.update(moneyFlow.getId(), patchMoneyFlow);
            call.enqueue(new Callback<MoneyFlow>() {
                @Override
                public void onResponse(Call<MoneyFlow> call, Response<MoneyFlow> response) {
                    Intent intent = new Intent();
                    if (response.isSuccessful()) {
                        MoneyFlow moneyFlow = response.body();
                        Log.v("RETROFIT", "onResponse:성공, 결과 \n" + moneyFlow.toString()); //나옴
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            intent.putExtra("successfulDate", date);
                            String []devideDate=date.split("-");
                            intent.putExtra("year",devideDate[0]);
                            intent.putExtra("month", devideDate[1]);
                            intent.putExtra("categoryID",moneyFlow.getCategoriesId());
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

        });

        ImageButton delete=findViewById(R.id.moenyDelete);
        delete.setOnClickListener(v->{
            Call<MoneyFlow> call = FinanceActivity.moneyService.delete(moneyFlow.getId());
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

        });



        boolean isSpend= moneyFlow.isSpend();

        if(isSpend) trueSpend.setChecked(true); //false면 지출
        else falseSepnd.setChecked(true);


        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        categoriesId= moneyFlow.getCategoriesId();
        String[] categoryValues = getResources().getStringArray(R.array.categories_values);
        for(int i=0; i<categoryValues.length; i++){
            if(Long.parseLong(categoryValues[i])==categoriesId){
                spinner.setSelection(i);
                break;
            }
        }

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