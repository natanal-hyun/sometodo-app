package com.sometodo.app.auth.service;

import com.sometodo.app.auth.entity.Account;
import com.sometodo.app.auth.entity.Profile;
import com.sometodo.app.auth.payload.request.ProfileEditRequest;
import com.sometodo.app.auth.payload.response.UserResponse;
import com.sometodo.app.auth.repository.AccountRepository;
import com.sometodo.app.auth.repository.ProfileRepository;
import com.sometodo.app.auth.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProfileService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 프로필 업데이트
     *
     * @param request 프로필 수정 요청 DTO
     * @throws IllegalArgumentException email, nickname 오류 시 예외 발생
     * @return Access Token (업데이트 된 유저 정보)
     */
    public Map<String, String> update(@Valid ProfileEditRequest request, String email) {
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        // 프로필 정보 조회
        Profile profile = profileRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("프로필 정보를 찾을 수 없습니다."));

        // 닉네임 중복 체크
        if (!profile.getNickname().equals(request.getNickname()) && profileRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        profile.setNickname(request.getNickname());
        profile.setImageUrl(request.getImageUrl());
        profile.setStatusMessage(request.getStatusMessage());
        profile.setUpdatedAt(LocalDateTime.now());

        profileRepository.save(profile);

        // 사용자 정보
        UserResponse userResponse = UserResponse.builder()
                .email(account.getEmail())
                .nickname(profile.getNickname())
                .imageUrl(profile.getImageUrl())
                .statusMessage(profile.getStatusMessage())
                .role(account.getRole())
                .build();

        String accessToken =  jwtUtil.generateAccessToken(userResponse);

        Map<String, String> token = new HashMap<>();
        token.put("accessToken", accessToken);

        return token;
    }
}
