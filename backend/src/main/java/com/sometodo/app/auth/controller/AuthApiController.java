package com.sometodo.app.auth.controller;

import com.nimbusds.jwt.JWTClaimsSet;
import com.sometodo.app.auth.entity.Account;
import com.sometodo.app.auth.entity.RefreshToken;
import com.sometodo.app.auth.payload.request.*;
import com.sometodo.app.auth.repository.AccountRepository;
import com.sometodo.app.auth.repository.RefreshTokenRepository;
import com.sometodo.app.auth.security.GoogleTokenVerifier;
import com.sometodo.app.auth.security.UserPrincipal;
import com.sometodo.app.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 인증(Auth) 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    AuthService authService;
    @Autowired
    GoogleTokenVerifier tokenVerifier;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    AccountRepository accountRepository;

    /**
     * 회원가입 API
     *
     * @param request SignupRequest 회원가입 요청 데이터 (DTO)
     * @return 성공 메시지 또는 에러 응답
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request, BindingResult result) {
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors().getFirst().getDefaultMessage());

        try {
            authService.registerAccount(request);
            return ResponseEntity.ok("회원가입 성공!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 로그인 API
     *
     * @param request 로그인 요청 데이터 (DTO)
     * @return 성공 시 Access & Refresh Token 반환 또는 에러 응답
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().getFirst().getDefaultMessage());
        }

        try {
            Map<String, String> tokens = authService.login(request); // ✅ Access & Refresh Token 반환
            return ResponseEntity.ok(tokens); // ✅ JSON 형식으로 응답
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Refresh Token을 이용한 Access Token 갱신 API
     *
     * @param request 클라이언트에서 전송한 Refresh Token (JSON 형식)
     * @return 성공 시 새로운 Access Token 반환 또는 에러 응답
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        try {
            String newAccessToken = authService.refreshAccessToken(request.getRefreshToken());

            Map<String, String> token = new HashMap<>();
            token.put("accessToken", newAccessToken);

            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * 비밀번호 변경 API
     *
     * @param request PasswordChangeRequest 비밀번호 변경 요청 데이터 (DTO)
     * @param userPrincipal 현재 인증된 사용자 정보
     * @return 성공 메세지 또는 에러 응답
     */
    @PatchMapping("/password")
    public ResponseEntity<?> passwordChange(@Valid @RequestBody PasswordChangeRequest request, BindingResult result,
                                            @AuthenticationPrincipal UserPrincipal userPrincipal) {  // ✅ 현재 로그인한 유저 정보 가져오기)
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getAllErrors().getFirst().getDefaultMessage());

        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        try {
            String email = userPrincipal.getEmail();
            authService.changePassword(request, email);
            return ResponseEntity.ok("비밀번호 변경 완료!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Google 로그인 API
     *
     * @param request Google 로그인 요청 데이터 (DTO)
     * @return 성공 시 Access & Refresh Token 반환 또는 에러 응답
     */
    @PostMapping("/google/login")
    public ResponseEntity<?> loginForGoogle(@Valid @RequestBody GoogleLoginRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().getFirst().getDefaultMessage());
        }

        try {
            JWTClaimsSet claims = tokenVerifier.verify(request.getIdToken());

            // claims에서 필요한 정보 추출 (예: 이메일, 이름, 프로필 이미지 등)
            String email = claims.getStringClaim("email");
            String nickname = claims.getStringClaim("name");
            String imageUrl = claims.getStringClaim("picture");

            Map<String, String> tokens = authService.loginForGoogle(email, nickname, imageUrl); // ✅ Access & Refresh Token 반환
            return ResponseEntity.ok(tokens); // ✅ JSON 형식으로 응답
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 로그아웃 처리
     *
     * @param userPrincipal 계정 정보
     * @return 성공 메시지
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        // 로그인된 계정이 없으면 로그아웃 실패
        if (userPrincipal == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");

        String email = userPrincipal.getEmail();
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("프로필 정보를 찾을 수 없습니다."));;

        // Refresh Token 삭제
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccount(account);

        if (refreshToken.isPresent()) {
            // Refresh Token 삭제
            refreshTokenRepository.delete(refreshToken.get());
        } else {
            return ResponseEntity.badRequest().body("로그아웃 실패: Refresh Token이 존재하지 않습니다.");
        }

        return ResponseEntity.ok("로그아웃 성공");
    }
}
