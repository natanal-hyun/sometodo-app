package com.sometodo.app.auth.entity;

import com.sometodo.app.auth.enums.AuthProvider;
import com.sometodo.app.auth.enums.UserRole;
import com.sometodo.app.auth.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키 (PK)

    @Column(nullable = false, unique = true)
    private String email; // 이메일 (로그인 ID 역할)

    @Column
    private String password; // 비밀번호 (암호화 필요)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER; // 유저 권한 (기본값: USER)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE; // 계정 상태 (ACTIVE, BANNED, DEACTIVATED, DELETED)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider; // 로그인 방식 (GOOGLE, EMAIL 등)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 가입일

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now(); // 마지막 수정일

    @Column
    private LocalDateTime lastLoginAt; // 마지막 로그인 시간
}
