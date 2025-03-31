package com.example.todolist.auth.token;

import org.json.JSONObject;

public class UserSession {
    private static String accessToken;
    private static JSONObject userClaims;

    public static void setAccessToken(String token) {
        accessToken = token;
        userClaims = JwtDecoder.decodeJwtPayload(token); // ✅ JWT 해석 후 캐싱
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static JSONObject getUserClaims() {
        return userClaims;
    }
}
