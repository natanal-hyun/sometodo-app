package com.sometodo.app.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleLoginRequest {
    @NotBlank(message = "ID 토큰이 필요합니다.")
    private String idToken; // ✅ 서버에서 구글 인증 검증용으로 반드시 필요
}
