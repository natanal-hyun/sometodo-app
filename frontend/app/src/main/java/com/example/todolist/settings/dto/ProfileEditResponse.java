package com.example.todolist.settings.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ProfileEditResponse {
    @SerializedName("accessToken")
    private final String accessToken;

    public ProfileEditResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() { return accessToken; }

    @NonNull
    @Override
    public String toString() {
        return "ProfileEditResponse{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}


