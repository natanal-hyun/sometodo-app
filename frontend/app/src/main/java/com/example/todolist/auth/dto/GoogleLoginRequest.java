package com.example.todolist.auth.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class GoogleLoginRequest {
    @SerializedName("idToken")
    private final String idToken;

    public GoogleLoginRequest(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() { return idToken; }

    @NonNull
    @Override
    public String toString() {
        return "GoogleLoginRequest{" +
                "idToken='" + idToken + '\'' +
                '}';
    }
}
