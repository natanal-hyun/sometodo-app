package com.sometodo.app.auth.dto;

import com.sometodo.app.auth.enums.AuthProvider;
import com.sometodo.app.auth.enums.UserRole;
import com.sometodo.app.auth.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private Long id; // 계정 ID
    private String email; // 이메일
    private String password; // 비밀번호
    private UserRole role; // 유저 권한
    private UserStatus status; // 계정 상태
    private AuthProvider authProvider; // 로그인 방식
    private LocalDateTime createdAt; // 가입일
    private LocalDateTime updatedAt; // 마지막 수정일
    private LocalDateTime lastLoginAt; // 마지막 로그인 시간
}
