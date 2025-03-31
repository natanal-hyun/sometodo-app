package com.example.todolist.settings.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;
import com.example.todolist.auth.network.AuthApiService;
import com.example.todolist.auth.network.RetrofitClient;
import com.example.todolist.settings.dto.PasswordChangeRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 📌 비밀번호 변경(Password Change) 화면
 *
 * - 사용자가 기존 비밀번호를 변경할 수 있는 액티비티
 * - 현재 비밀번호와 새 비밀번호, 새 비밀번호 확인 입력 가능
 * - 비밀번호 변경 시 유효성 검사 적용 가능
 */
public class PasswordChangeActivity extends AppCompatActivity {
    EditText passwordNowInput, passwordNewInput, passwordCheckInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_password_change);

        passwordNowInput = findViewById(R.id.passwordNowInput);
        passwordNewInput = findViewById(R.id.passwordNewInput);
        passwordCheckInput = findViewById(R.id.passwordCheckInput);
    }

    /**
     * 뒤로가기 버튼 클릭 이벤트 핸들러
     * @param view 클릭된 뷰
     */
    public void onBackButtonClick(View view) {
        finish();
    }

    /**
     * 변경 버튼 클릭 이벤트 핸들러
     * @param view 클릭된 뷰
     */
    public void onChangeButtonClick(View view) {
        String pwNow = passwordNowInput.getText().toString().trim();
        String pwNew = passwordNewInput.getText().toString().trim();
        String pwCheck = passwordCheckInput.getText().toString().trim();

        if (pwNow.isEmpty() || pwNew.isEmpty() || pwCheck.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pwNew.length() < 8 || pwNew.length() > 20) {
            Toast.makeText(this, "비밀번호는 8자 이상 20자 이하이어야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pwNew.equals(pwNow)) {
            Toast.makeText(this, "기존 비밀번호와 새 비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pwNew.equals(pwCheck)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // API 요청 객체 생성
        PasswordChangeRequest request = new PasswordChangeRequest(pwNow, pwNew, pwCheck);

        // API 요청 실행
        AuthApiService apiService = RetrofitClient.getClient().create(AuthApiService.class);
        Call<Void> call = apiService.changePassword(request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PasswordChangeActivity.this, "비밀번호 변경 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PasswordChangeActivity.this, "비밀번호 변경 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(PasswordChangeActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
