package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.todolist.auth.activity.LoginActivity;
import com.example.todolist.auth.network.RetrofitClient;
import com.example.todolist.auth.token.TokenManager;
import com.example.todolist.auth.token.UserSession;
import com.example.todolist.schedule.Activity.ScheduleActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SplashScreen API 적용
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        RetrofitClient.init(getApplicationContext());

        // TokenManager 초기화 (앱 실행 시 한 번만 호출)
        TokenManager.init(this);

        // SharedPreferences에서 저장된 Access Token 확인
//        TokenManager.clearTokens();
        String accessToken = TokenManager.getAccessToken();

        // Access Token 존재 하지 않을 때
        if (accessToken == null || accessToken.isEmpty()) {
            // 로그아웃 상태 → LoginActivity로 이동
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish(); // 스플래시 종료
            return; // 여기서 종료하여 이후 코드 실행되지 않도록 방지
        }

        // ✅ UserSession 초기화 → JWT 해석하여 유저 정보 저장
        UserSession.setAccessToken(accessToken);

        startActivity(new Intent(MainActivity.this, ScheduleActivity.class));
        finish(); // 스플래시 종료
    }
}