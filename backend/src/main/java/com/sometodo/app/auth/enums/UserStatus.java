package com.sometodo.app.auth.enums;

public enum UserStatus {
    ACTIVE,        // 활성 계정
    BANNED,        // 정지된 계정
    DEACTIVATED,    // 비활성화된 계정 (회원 탈퇴)
    DELETED       // 완전 삭제 (소프트 삭제)
}
