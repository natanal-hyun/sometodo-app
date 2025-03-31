package com.sometodo.app.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER("ROLE_USER"),  // 일반 사용자
    MANAGER("ROLE_MANAGER"), // 제한적인 관리자 권한
    ADMIN("ROLE_ADMIN"), // 관리자
    SUPER_ADMIN("ROLE_SUPER_ADMIN"); // 최상위 관리자 (모든 권한 보유)

    private final String roleName;
}
