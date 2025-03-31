package com.example.todolist.settings.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ProfileEditRequest {
    @SerializedName("nickname")
    private final String nickname;

    @SerializedName("imageUrl")
    private final String imageUrl;

    @SerializedName("statusMessage")
    private final String statusMessage;

    public ProfileEditRequest(String nickname, String imageUrl, String statusMessage) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.statusMessage = statusMessage;
    }

    public String getNickname() {
        return nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    @NonNull
    @Override
    public String toString() {
        return "ProfileEditRequest{" +
                "nickname='" + nickname + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                '}';
    }
}

