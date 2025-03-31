package com.example.todolist.schedule.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;
import com.example.todolist.auth.network.RetrofitClient;
import com.example.todolist.schedule.Class.ScheduleClass;
import com.example.todolist.schedule.Service.ScheduleService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends AppCompatActivity{
    RadioButton rb_exercise,rb_meet,rb_rest,rb_study,rb_hobby;

    Button Btn_back,Btn_add;

    TextView tv_display_category,tv_display_Date;

    EditText et_content;

    String CreateCategory,CreateContent;

    int CreateDataYear,CreateDataMonth,CreateDataDay;

    ScheduleService service;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);

        //Retrofit,Service
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
        service = RetrofitClient.getClient().create(ScheduleService.class);

        //INTENT - MainActivity
        Intent intent = getIntent();
        CreateDataYear = intent.getIntExtra("year",0);
        CreateDataMonth = intent.getIntExtra("month",0);
        CreateDataDay = intent.getIntExtra("day",0);


        //Button
        Btn_back = findViewById(R.id.back_button2);
        Btn_add = findViewById(R.id.add_Button);


        //RadioButton
        rb_exercise = findViewById(R.id.rb_exercise);
        rb_meet = findViewById(R.id.rb_meet);
        rb_rest = findViewById(R.id.rb_rest);
        rb_study = findViewById(R.id.rb_study);
        rb_hobby = findViewById(R.id.rb_hobby);


        //TextView
        tv_display_category = findViewById(R.id.tv_selectedCategory);
        tv_display_Date = findViewById(R.id.tv_displayDate2);
        tv_display_Date.setText(CreateDataYear+"/"+CreateDataMonth+"/"+CreateDataDay+" Schedule");


        //EditText
        et_content = findViewById(R.id.et_create_content);


        //onclick - RadioButton
        rb_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_display_category.setText(rb_exercise.getText().toString());
                CreateCategory = "운동";

            }
        });
        rb_meet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_display_category.setText(rb_meet.getText().toString());
                CreateCategory = "만남";
            }
        });
        rb_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_display_category.setText(rb_rest.getText().toString());
                CreateCategory = "여가";

            }
        });
        rb_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_display_category.setText(rb_study.getText().toString());
                CreateCategory = "공부";

            }
        });
        rb_hobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_display_category.setText(rb_hobby.getText().toString());
                CreateCategory = "취미";

            }
        });


        //onclick - Button
        Btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Animation
                {
                    // 커지는 애니메이션 생성
                    ScaleAnimation scaleUp = new ScaleAnimation(
                            1f, 1.2f, // X축 크기: 1배에서 1.5배로
                            1f, 1.5f, // Y축 크기: 1배에서 1.5배로
                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f, // 기준점을 뷰의 중심으로 설정
                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                    scaleUp.setDuration(700);  // 0.5초 동안 커짐
                    scaleUp.setFillAfter(true); // 애니메이션 후 최종 상태 유지

                    // 작아지는 애니메이션 생성
                    ScaleAnimation scaleDown = new ScaleAnimation(
                            1.1f, 1f, // X축 크기: 1.5배에서 1배로
                            1.1f, 1f, // Y축 크기: 1.5배에서 1배로
                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f, // 기준점을 뷰의 중심으로 설정
                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                    scaleDown.setDuration(400);  // 0.3초 동안 작아짐
                    scaleDown.setFillAfter(true); // 애니메이션 후 최종 상태 유지

                    // 애니메이션을 순차적으로 실행
                    Btn_back.startAnimation(scaleUp);
                    Btn_back.startAnimation(scaleDown);
                }
                Btn_back.setBackgroundColor(Color.parseColor("#B2FFAF"));
                Intent intent = new Intent(CreateActivity.this, ScheduleActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Animation
                {
                    // 커지는 애니메이션 생성
                    ScaleAnimation scaleUp = new ScaleAnimation(
                            1f, 1.2f, // X축 크기: 1배에서 1.5배로
                            1f, 1.5f, // Y축 크기: 1배에서 1.5배로
                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f, // 기준점을 뷰의 중심으로 설정
                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                    scaleUp.setDuration(700);  // 0.5초 동안 커짐
                    scaleUp.setFillAfter(true); // 애니메이션 후 최종 상태 유지

                    // 작아지는 애니메이션 생성
                    ScaleAnimation scaleDown = new ScaleAnimation(
                            1.1f, 1f, // X축 크기: 1.5배에서 1배로
                            1.1f, 1f, // Y축 크기: 1.5배에서 1배로
                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f, // 기준점을 뷰의 중심으로 설정
                            ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                    scaleDown.setDuration(400);  // 0.3초 동안 작아짐
                    scaleDown.setFillAfter(true); // 애니메이션 후 최종 상태 유지

                    // 애니메이션을 순차적으로 실행
                    Btn_add.startAnimation(scaleUp);
                    Btn_add.startAnimation(scaleDown);
                }
                Btn_add.setBackgroundColor(Color.parseColor("#B2FFAF"));

                CreateContent = et_content.getText().toString();
                if (CreateContent.isEmpty() || CreateCategory==null){
                    Toast.makeText(CreateActivity.this, "현재 빈값이 존재합니다.", Toast.LENGTH_SHORT).show();
                }else{
                    ScheduleClass schedule = new ScheduleClass(
                            CreateContent,
                            CreateCategory,
                            CreateDataYear,
                            CreateDataMonth,
                            CreateDataDay,
                            null,
                            false);

                    Call<ScheduleClass> call = service.createSchedule(schedule);
                    call.enqueue(new Callback<ScheduleClass>() {
                        @Override
                        public void onResponse(Call<ScheduleClass> call, Response<ScheduleClass> response) {
                            if (response.isSuccessful()){
                                Intent intent1 = new Intent();
                                setResult(RESULT_OK,intent1);
                                finish();
                            }else{
                                Toast.makeText(CreateActivity.this, "일정추가 실패", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ScheduleClass> call, Throwable t) {
                            Log.v("onFailure",t.getMessage());
                        }
                    });

                }

            }
        });    //CREATE_DATA



    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }
}
