package com.example.todolist.auth.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class RefreshResponse {
    @SerializedName("accessToken")
    private final String accessToken;

    public RefreshResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    @NonNull
    @Override
    public String toString() {
        return "LoginResponse{" +
                "accessToken='" + accessToken +
                '}';
    }

    public String getAccessToken() { return accessToken; }
}
