package com.sometodo.app.auth.controller;

import com.sometodo.app.auth.payload.request.ProfileEditRequest;
import com.sometodo.app.auth.security.UserPrincipal;
import com.sometodo.app.auth.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 프로필(Profile) 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/api/profile")
public class ProfileApiController {
    @Autowired
    ProfileService profileService;

    /**
     * 프로필 수정 API
     *
     * @param request ProfileEditRequest 프로필 수정 요청 데이터 (DTO)
     * @param userPrincipal 현재 인증된 사용자 정보
     * @return Access Token (업데이트 된 유저 정보) 또는 에러 응답
     */
    @PatchMapping
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileEditRequest request, BindingResult result,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {  // ✅ 현재 로그인한 유저 정보 가져오기)
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors().getFirst().getDefaultMessage());

        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            Map<String, String> token = profileService.update(request, email); // ✅ Access Token 반환
            return ResponseEntity.ok(token); // ✅ JSON 형식으로 응답
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

