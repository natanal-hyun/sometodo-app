package com.example.todolist.auth.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class SignupRequest {
    @SerializedName("email")
    private final String email;
    @SerializedName("password")
    private final String password;
    @SerializedName("passwordCheck")
    private final String passwordCheck;
    @SerializedName("nickname")
    private final String nickname;

    public SignupRequest(String email, String password, String passwordCheck, String nickname) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.nickname = nickname;
    }

    @NonNull
    @Override
    public String toString() {
        return "SignupRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwordCheck='" + passwordCheck + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPasswordCheck() { return passwordCheck; }
    public String getNickname() { return nickname; }
}
