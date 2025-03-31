package com.example.todolist.auth.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    private final String email;
    @SerializedName("password")
    private final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
