package com.example.todolist.settings.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class PasswordChangeRequest {
    @SerializedName("passwordNow")
    private final String passwordNow; // ✅ 현재 비밀번호

    @SerializedName("passwordNew")
    private final String passwordNew; // ✅ 새 비밀번호

    @SerializedName("passwordCheck")
    private final String passwordCheck; // ✅ 새 비밀번호 확인

    public PasswordChangeRequest(String passwordNow, String passwordNew, String passwordCheck) {
        this.passwordNow = passwordNow;
        this.passwordNew = passwordNew;
        this.passwordCheck = passwordCheck;
    }

    public String getPasswordNow() {
        return passwordNow;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }

    @NonNull
    @Override
    public String toString() {
        return "PasswordChangeRequest{" +
                "passwordNow='" + passwordNow + '\'' +
                ", passwordNew='" + passwordNew + '\'' +
                ", passwordCheck='" + passwordCheck + '\'' +
                '}';
    }
}
