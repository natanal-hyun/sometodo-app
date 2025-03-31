package com.example.todolist.auth.token;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

public class JwtDecoder {
    public static JSONObject decodeJwtPayload(String jwtToken) {
        try {
            // JWT는 '.'으로 구분되므로, 두 번째 부분(Payload)을 가져옴
            String[] splitToken = jwtToken.split("\\.");
            if (splitToken.length < 2) return null;

            String payload = splitToken[1];

            // Base64 디코딩
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE);
            String decodedString = new String(decodedBytes);

            // JSON으로 변환하여 반환
            return new JSONObject(decodedString);
        } catch (Exception e) {
            Log.e("JwtDecoder", "JWT 디코딩 중 오류 발생", e);
            return null;
        }
    }
}
