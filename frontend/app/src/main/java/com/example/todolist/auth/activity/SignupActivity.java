package com.example.todolist.auth.activity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.R;
import com.example.todolist.auth.dto.SignupRequest;
import com.example.todolist.auth.network.AuthApiService;
import com.example.todolist.auth.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    EditText etEmail, etPassword, etPasswordCheck, etNickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup);

        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);
        etPasswordCheck = findViewById(R.id.passwordCheckEditText);
        etNickname = findViewById(R.id.nicknameEditText);
    }

    public void onSignupButtonClick(View view) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordCheck = etPasswordCheck.getText().toString().trim();
        String nickname = etNickname.getText().toString().trim();

        // 1️⃣ 입력값 검증
        if (email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty() || nickname.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "올바른 이메일 형식을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passwordCheck)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8 || password.length() > 20) {
            Toast.makeText(this, "비밀번호는 8자 이상 20자 이하이어야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nickname.matches("^[a-zA-Z0-9가-힣]+$")) {
            Toast.makeText(this, "닉네임은 영문, 숫자, 한글만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nickname.length() < 2 || nickname.length() > 12) {
            Toast.makeText(this, "닉네임은 2자 이상 12자 이하이어야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2️⃣ API 요청을 위한 SignupRequest 객체 생성
        SignupRequest signupRequest = new SignupRequest(email, password, passwordCheck, nickname);

        // 3️⃣ Retrofit을 통해 회원가입 요청
        AuthApiService authApiService = RetrofitClient.getClient().create(AuthApiService.class);
        Call<Void> call = authApiService.signup(signupRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();

                    // 4️⃣ 성공 시 로그인 화면으로 이동
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "회원가입 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(SignupActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onLoginTextClick(View view) {
        finish();
    }

}
