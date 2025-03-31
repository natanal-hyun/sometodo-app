package com.sometodo.app.auth.security;

import com.sometodo.app.auth.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    private String email;
    private String nickname;
    private String imageUrl;
    private String statusMessage;
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleName())); // ✅ 권한 변환 생략 가능
    }

    @Override
    public String getPassword() {
        return null;  // ✅ 패스워드 미사용 → 빈 문자열 반환
    }

    @Override
    public String getUsername() {
        return email;  // ✅ 의미 있는 값 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // ✅ 계정이 만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // ✅ 계정이 잠기지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // ✅ 비밀번호가 만료되지 않음
    }

    @Override
    public boolean isEnabled() {
        return true;  // ✅ 계정이 활성화됨
    }
}


