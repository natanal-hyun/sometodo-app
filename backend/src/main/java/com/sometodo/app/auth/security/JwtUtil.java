package com.sometodo.app.auth.security;

import com.sometodo.app.auth.payload.response.UserResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private static String SECRET_KEY;

    private static final long ACCESS_EXPIRATION_TIME = 1000 * 10 * 60 * 60; // 1시간
    private static final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    public String generateAccessToken(UserResponse userResponse) {
        return Jwts.builder()
                .setSubject(userResponse.getEmail()) // 이메일을 subject로 설정
                .claim("nickname", userResponse.getNickname())
                .claim("imageUrl", userResponse.getImageUrl())
                .claim("statusMessage", userResponse.getStatusMessage())
                .claim("role", userResponse.getRole().toString())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME)) // 만료 시간 설정
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256) // 서명 알고리즘 설정
                .compact();
    }

    public String generateRefreshToken(UserResponse userResponse) {
        return Jwts.builder()
                .setSubject(userResponse.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        return extractClaims(token).getExpiration().after(new Date());
    }
}
