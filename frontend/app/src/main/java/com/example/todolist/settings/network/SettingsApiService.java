package com.example.todolist.settings.network;

import com.example.todolist.settings.dto.ProfileEditRequest;
import com.example.todolist.settings.dto.ProfileEditResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface SettingsApiService {
    @PATCH("/api/profile")
    Call<ProfileEditResponse> updateProfile(@Body ProfileEditRequest request);
    @POST("/api/auth/logout")
    Call<Void> logout();
}
