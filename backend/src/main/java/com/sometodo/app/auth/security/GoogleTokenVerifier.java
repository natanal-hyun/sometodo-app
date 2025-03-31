package com.sometodo.app.auth.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class GoogleTokenVerifier {

    private static final String GOOGLE_JWK_URL = "https://www.googleapis.com/oauth2/v3/certs";

    @Value("${google.client.id}")
    private String clientId;

    public JWTClaimsSet verify(String idToken) throws Exception {
        // try-with-resources 구문으로 HttpClient와 HttpResponse 자동 관리
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GOOGLE_JWK_URL))
                    .build();

            // HttpResponse<String>를 try-with-resources로 사용하여 자동으로 자원 관리
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 받은 JSON을 JWKSet으로 파싱
            JWKSet jwkSet = JWKSet.parse(response.body());

            // JWKSet에서 키를 가져와서 검증 수행
            JWKSource<SecurityContext> keySource = new ImmutableJWKSet<>(jwkSet);

            // JWT 처리기 설정
            DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

            // JWSVerificationKeySelector를 사용하여 서명 검증
            JWSVerificationKeySelector<SecurityContext> keySelector =
                    new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource);
            jwtProcessor.setJWSKeySelector(keySelector);

            JWTClaimsSet claimsSet = jwtProcessor.process(idToken, null);

            // audience 검사
            if (!claimsSet.getAudience().contains(clientId)) {
                throw new IllegalArgumentException("유효하지 않은 audience");
            }

            // 만료 시간 검사
            if (claimsSet.getExpirationTime().getTime() < System.currentTimeMillis()) {
                throw new IllegalArgumentException("Token 만료됨");
            }

            return claimsSet;
        }
    }
}
