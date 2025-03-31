package com.example.todolist.auth.network;

import android.content.Context;

import com.example.todolist.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = BuildConfig.BASE_URL; // 환경 변수에서 BASE_URL 불러오기

    private static Retrofit retrofit = null;

    private static Context appContext = null;

    public static void init(Context context) {
        appContext = context.getApplicationContext(); // 안전하게 보관
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(appContext)) // ✅ 토큰 자동 추가
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 자동 변환
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
