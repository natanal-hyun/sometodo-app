package com.example.todolist.auth.network;

import com.example.todolist.auth.dto.GoogleLoginRequest;
import com.example.todolist.auth.dto.LoginRequest;
import com.example.todolist.auth.dto.LoginResponse;
import com.example.todolist.auth.dto.RefreshRequest;
import com.example.todolist.auth.dto.RefreshResponse;
import com.example.todolist.auth.dto.SignupRequest;
import com.example.todolist.settings.dto.PasswordChangeRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("api/auth/signup")
    Call<Void> signup(@Body SignupRequest request);

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/auth/refresh")
    Call<RefreshResponse> refresh(@Body RefreshRequest request);

    @PATCH("api/auth/password")
    Call<Void> changePassword(@Body PasswordChangeRequest request);

    @POST("api/auth/google/login")
    Call<LoginResponse> loginForGoogle(@Body GoogleLoginRequest request);
}
