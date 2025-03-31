package com.example.todolist.auth.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class RefreshRequest {
    @SerializedName("refreshToken")
    private String refreshToken;

    // 생성자, getter, setter 추가
    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @NonNull
    @Override
    public String toString() {
        return "RefreshRequest{" +
                "refreshToken='" + refreshToken + '}';
    }
}
