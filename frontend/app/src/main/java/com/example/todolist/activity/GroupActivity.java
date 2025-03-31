package com.example.todolist.activity;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.NavigationHandler;
import com.example.todolist.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GroupActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_group);
        bottomNavigationView.setOnItemSelectedListener(item -> NavigationHandler.handleNavigation(this, item));

        // 뒤로가기 이벤트 처리
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(0, 0); // 애니메이션 제거
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setOnItemSelectedListener(null);
        bottomNavigationView.setSelectedItemId(R.id.navigation_group);
        bottomNavigationView.setOnItemSelectedListener(item -> NavigationHandler.handleNavigation(this, item));
    }
}
