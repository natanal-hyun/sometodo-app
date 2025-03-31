package com.example.todolist.auth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.BuildConfig;
import com.example.todolist.R;
import com.example.todolist.auth.dto.GoogleLoginRequest;
import com.example.todolist.auth.dto.LoginRequest;
import com.example.todolist.auth.dto.LoginResponse;
import com.example.todolist.auth.network.AuthApiService;
import com.example.todolist.auth.network.RetrofitClient;
import com.example.todolist.auth.token.TokenManager;
import com.example.todolist.auth.token.UserSession;
import com.example.todolist.schedule.Activity.ScheduleActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> signInLauncher;

    EditText etEmail, etPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        TokenManager.init(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.passwordEditText);

        // 로그인 옵션 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()     // 사용자 이메일 요청
                .requestProfile()   // 사용자 프로필 요청
//                .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
                .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .build();

        // Google 로그인 클라이언트 초기화
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // 로그인 결과 처리 콜백 등록
        signInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                        if (task.isSuccessful()) {
                            handleSignInResult(task);
                        } else {
                            Toast.makeText(this, "로그인 실패: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "로그인이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void onLoginButtonClick(View view) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "올바른 이메일 형식을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthApiService apiService = RetrofitClient.getClient().create(AuthApiService.class);
        Call<LoginResponse> call = apiService.login(new LoginRequest(email, password));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    loginSuccess(response.body());
                } else {
                    try (ResponseBody errorBody = response.errorBody()) {  // ✅ try-with-resources 사용
                        if (errorBody != null) {
                            Toast.makeText(LoginActivity.this, "로그인 실패: " + errorBody.string(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 실패: 알 수 없는 오류", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "로그인 실패: 응답 처리 오류", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSignupTextClick(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    // 로그인 인텐트 실행
    public void onLoginForGoogleButtonClick(View view) {
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            // 로그아웃 완료 후 새 로그인 시작
            Intent signInIntent = googleSignInClient.getSignInIntent();
            signInLauncher.launch(signInIntent);
        });
    }

    // 로그인 성공 여부 처리
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

//            String imageUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : null;

            AuthApiService apiService = RetrofitClient.getClient().create(AuthApiService.class);
            Call<LoginResponse> call = apiService.loginForGoogle(
                    new GoogleLoginRequest(account.getIdToken())
            );

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        loginSuccess(response.body());
                    } else {
                        try (ResponseBody errorBody = response.errorBody()) {  // ✅ try-with-resources 사용
                            if (errorBody != null) {
                                Toast.makeText(LoginActivity.this, "로그인 실패: " + errorBody.string(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "로그인 실패: 알 수 없는 오류", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "로그인 실패: 응답 처리 오류", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    Toast.makeText(LoginActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (ApiException e) {
            Log.e("GOOGLE_LOGIN", "로그인 실패", e);
        }
    }

    private void loginSuccess(LoginResponse loginResponse) {
        TokenManager.saveTokens(loginResponse.getAccessToken(), loginResponse.getRefreshToken());

//        Log.v("mytag", loginResponse.toString());

        // ✅ UserSession 초기화 → JWT 해석하여 유저 정보 저장
        UserSession.setAccessToken(loginResponse.getAccessToken());

        Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(LoginActivity.this, ScheduleActivity.class));
        finish();
    }
}
