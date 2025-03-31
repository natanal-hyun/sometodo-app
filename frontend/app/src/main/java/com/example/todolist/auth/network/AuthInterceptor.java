package com.example.todolist.auth.network;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.example.todolist.auth.activity.LoginActivity;
import com.example.todolist.auth.token.TokenManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context.getApplicationContext(); // ApplicationContext 사용
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        String accessToken = TokenManager.getAccessToken();
        Request original = chain.request();

        // refresh 엔드포인트는 토큰 검증 건너뛰기
        if (original.url().encodedPath().contains("/api/auth/refresh")) {
            return chain.proceed(original);
        }

        Request request = original.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        Response response = chain.proceed(request);

        // 401 Unauthorized → Access Token이 만료되었을 가능성이 있음
        if (response.code() == 401 || response.code() == 403) {
            synchronized (this) {
                String refreshToken = TokenManager.getRefreshToken();
                if (refreshToken != null) {
                    String newAccessToken = TokenManager.refreshAccessToken(refreshToken);
                    if (newAccessToken != null) {
                        TokenManager.setAccessToken(newAccessToken);

                        // 새로운 Access Token으로 다시 요청 실행
                        Request newRequest = original.newBuilder()
                                .header("Authorization", "Bearer " + newAccessToken)
                                .build();
                        return chain.proceed(newRequest);
                    } else {
                        // 토큰 갱신 실패: 강제 로그아웃 처리
                        forceLogout();
                    }
                } else {
                    // Refresh Token이 없으면 강제 로그아웃
                    forceLogout();
                }
            }
        }

        return response;
    }

    /**
     * 강제로 로그아웃 이벤트를 발생시켜 앱 전체를 로그아웃 상태로 만듭니다.
     */
    private void forceLogout() {
        // 토큰 삭제
        TokenManager.clearTokens();

        // UI 스레드에서 실행
        new Handler(Looper.getMainLooper()).post(() -> {
            Intent intent = new Intent(context, LoginActivity.class);
            // 기존의 모든 액티비티를 제거하고 새로운 태스크에서 LoginActivity 실행
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
    }
}
