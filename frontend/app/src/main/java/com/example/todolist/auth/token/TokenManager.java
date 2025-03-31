package com.example.todolist.auth.token;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.todolist.auth.dto.RefreshRequest;
import com.example.todolist.auth.dto.RefreshResponse;
import com.example.todolist.auth.network.AuthApiService;
import com.example.todolist.auth.network.RetrofitClient;

import java.io.IOException;

import retrofit2.Response;

public class TokenManager {
    private static final String PREF_NAME = "auth";
    private static SharedPreferences prefs;

    public static void init(Context context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
            Log.d("TokenManager", "SharedPreferences initialized");
        } else {
            Log.d("TokenManager", "SharedPreferences already initialized");
        }
    }

    public static void saveTokens(String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("access_token", accessToken);
        editor.putString("refresh_token", refreshToken);
        editor.apply();
    }

    public static String getAccessToken() {
        if (prefs == null) {
            Log.e("TokenManager", "SharedPreferences is null in getAccessToken()");
            return null;
        }
        return prefs.getString("access_token", "");
    }

    public static String getRefreshToken() {
        return prefs.getString("refresh_token", null);
    }

    public static void setAccessToken(String token) {
        prefs.edit().putString("access_token", token).apply();
    }

    public static void clearTokens() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("access_token");
        editor.remove("refresh_token");
        editor.apply();
    }

    public static String refreshAccessToken(String refreshToken) {
        try {
            AuthApiService apiService = RetrofitClient.getClient().create(AuthApiService.class);
            Response<RefreshResponse> response = apiService.refresh(new RefreshRequest(refreshToken)).execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body().getAccessToken();
            }
        } catch (IOException e) {
            Log.e("AuthInterceptor", "Access Token 재발급 중 오류 발생", e);
        }
        return null;
    }
}
