package com.example.todolist.settings.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;

/**
 * 📌 알림(Notification) 설정 화면
 *
 * - 미완성
 */
public class NotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_notification);
    }

    /**
     * 뒤로가기 버튼 클릭 이벤트 핸들러
     * @param view 클릭된 뷰
     */
    public void onBackButtonClick(View view) {
        finish();
    }
}
