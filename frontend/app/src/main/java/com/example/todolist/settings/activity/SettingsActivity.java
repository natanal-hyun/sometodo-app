package com.example.todolist.settings.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.todolist.NavigationHandler;
import com.example.todolist.R;
import com.example.todolist.auth.token.UserSession;
import com.example.todolist.settings.SettingsAdapter;
import com.example.todolist.settings.SettingsItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 📌 설정(Settings) 화면
 *
 * - 앱의 다양한 설정을 관리하는 메인 설정 액티비티
 * - 프로필 편집, 비밀번호 변경 등의 기능으로 이동 가능
 * - 향후 필요 시 알림 기능 추가 예정
 */
public class SettingsActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    private RecyclerView recyclerView;

    private ImageView profileImage;
    private TextView profileName, profileEmail, profileStatusMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
        bottomNavigationView.setOnItemSelectedListener(item -> NavigationHandler.handleNavigation(this, item));

        // UI 요소 초기화
        profileImage = findViewById(R.id.profileImage);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileStatusMessage = findViewById(R.id.profileStatusMessage);

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerViewSettings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 유저 정보 설정
        loadUserInfo();

        // 설정 목록 로드
        loadSettings();

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
        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
        bottomNavigationView.setOnItemSelectedListener(item -> NavigationHandler.handleNavigation(this, item));

        loadUserInfo();
    }

    private void loadUserInfo() {
        JSONObject userClaims = UserSession.getUserClaims();

        try {
            String nickname = userClaims.getString("nickname");
            String email = userClaims.getString("sub");
            String imageUrl = userClaims.optString("imageUrl", "");
            String statusMessage = userClaims.optString("statusMessage", "");

            profileName.setText(nickname);
            profileEmail.setText(email);
            profileStatusMessage.setText(statusMessage);

            // Glide를 사용하여 프로필 이미지 로드
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.icon_profile_placeholder) // 기본 이미지
                    .error(R.drawable.icon_profile_placeholder) // 오류 시 기본 이미지
                    .circleCrop() // 원형으로 자르기
                    .into(profileImage);
        } catch (JSONException e) {
            Log.e("UserSession", "사용자 정보 JSON 파싱 오류: " + e.getMessage(), e);
        }
    }

    private void loadSettings() {
        List<SettingsItem> settingsList = new ArrayList<>();

        settingsList.add(new SettingsItem("프로필 수정", R.drawable.icon_profile_48dp));
        settingsList.add(new SettingsItem("비밀번호 변경", R.drawable.icon_password_48dp));
        settingsList.add(new SettingsItem("푸시 알림 설정", R.drawable.icon_notifications_48dp));
        settingsList.add(new SettingsItem("로그아웃", R.drawable.icon_logout_48dp));

        SettingsAdapter adapter = new SettingsAdapter(settingsList, this);
        recyclerView.setAdapter(adapter);
    }
}
