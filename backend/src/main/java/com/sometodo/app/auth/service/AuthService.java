package com.sometodo.app.auth.service;

import com.sometodo.app.auth.entity.Account;
import com.sometodo.app.auth.entity.Profile;
import com.sometodo.app.auth.entity.RefreshToken;
import com.sometodo.app.auth.enums.AuthProvider;
import com.sometodo.app.auth.enums.UserRole;
import com.sometodo.app.auth.enums.UserStatus;
import com.sometodo.app.auth.payload.request.LoginRequest;
import com.sometodo.app.auth.payload.request.PasswordChangeRequest;
import com.sometodo.app.auth.payload.request.SignupRequest;
import com.sometodo.app.auth.payload.response.UserResponse;
import com.sometodo.app.auth.repository.AccountRepository;
import com.sometodo.app.auth.repository.ProfileRepository;
import com.sometodo.app.auth.repository.RefreshTokenRepository;
import com.sometodo.app.auth.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 회원가입 처리
     *
     * @param request 회원가입 요청 DTO
     * @throws IllegalArgumentException 이메일 중복 시 예외 발생
     */
    public void registerAccount(@Valid SignupRequest request) {
        // 이메일 중복 체크
        if (accountRepository.existsByEmail(request.getEmail()))
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        // 닉네임 중복 체크
        if (profileRepository.existsByNickname(request.getNickname()))
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");

        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 암호화된 비밀번호 저장
                .authProvider(AuthProvider.EMAIL)
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        accountRepository.save(account);

        Profile profile = Profile.builder()
                .account(account)
                .nickname(request.getNickname())
                .updatedAt(LocalDateTime.now())
                .build();

        profileRepository.save(profile);
    }

    /**
     * 로그인 처리
     *
     * @param request 로그인 요청 DTO
     * @throws IllegalArgumentException 이메일 없을 시 또는 비밀번호 틀렸을 시 예외 발생
     * @return Map - Refresh Token, Access Token
     */
    public Map<String, String>  login(@Valid LoginRequest request) {
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        return getTokens(account);
    }

    /**
     * Access Token 재발급
     *
     * @param refreshToken refresh token
     * @throws IllegalArgumentException Token의 유효하지 않거나 만료되었을 경우 예외 발생
     * @return Access Token
     */
    public String refreshAccessToken(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Refresh Token입니다."));

        if (storedToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh Token이 만료되었습니다.");
        }

        Account account = storedToken.getAccount();
        Profile profile = profileRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("프로필 정보를 찾을 수 없습니다."));

        UserResponse userResponse = UserResponse.builder()
                .email(account.getEmail())
                .nickname(profile.getNickname())
                .imageUrl(profile.getImageUrl())
                .statusMessage(profile.getStatusMessage())
                .role(account.getRole())
                .build();

        return jwtUtil.generateAccessToken(userResponse);
    }

    /**
     * 비밀번호 변경
     *
     * @param request 비밀번호 변경 요청 DTO
     * @throws IllegalArgumentException 잘못된 이메일 또는 비밀번호 오류 시 예외 발생
     */
    public void changePassword(@Valid PasswordChangeRequest request, String email) {
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일이 잘못되었습니다."));

        if (!passwordEncoder.matches(request.getPasswordNow(), account.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        account.setPassword(passwordEncoder.encode(request.getPasswordNew()));
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);
    }

    /**
     * 구글 로그인 처리
     *
     * @param email 이메일
     * @param nickname 닉네임
     * @param imageUrl 이미지
     * @throws IllegalArgumentException 이미 중복된 이메일로 가입 시 예외 발생
     * @return Map - Refresh Token, Access Token
     */
    public Map<String, String>  loginForGoogle(String email, String nickname, String imageUrl) {
        // 1. 이메일 중복 확인
        if (accountRepository.existsByEmail(email)) {
            // 2. 계정이 존재하면 해당 계정 찾기
            Account account = accountRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException(""));

            // 3. 구글 로그인 여부 확인 (AuthProvider가 GOOGLE인지 확인)
            if (account.getAuthProvider() != AuthProvider.GOOGLE) {
                throw new IllegalArgumentException("이 이메일은 구글 로그인 계정이 아닙니다.");
            }

            // 4. 구글 계정이면 로그인 진행
            return getTokens(account);
        } else {
            // 5. 계정이 없다면 새로운 회원 가입
            Account account = Account.builder()
                    .email(email)
                    .authProvider(AuthProvider.GOOGLE)
                    .role(UserRole.USER)
                    .status(UserStatus.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            // Account와 Profile 저장
            accountRepository.save(account);

            Profile profile = Profile.builder()
                    .account(account)
                    .imageUrl(imageUrl)
                    .nickname(generateUniqueNickname(nickname))
                    .updatedAt(LocalDateTime.now())
                    .build();

            profileRepository.save(profile);

            // 6. 로그인 진행 (새로운 유저의 토큰 생성)
            return getTokens(account);
        }
    }

    /**
     * 계정 정보를 받아 프로필 정보를 가져와 Token 을 만들어 반납
     *
     * @param account 계정 정보
     * @throws IllegalArgumentException 프로필 없을 시 예외 발생
     * @return Map - Refresh Token, Access Token
     */
    private Map<String, String> getTokens(Account account) {
        // 프로필 정보 조회
        Profile profile = profileRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("프로필 정보를 찾을 수 없습니다."));

        // 사용자 정보
        UserResponse userResponse = UserResponse.builder()
                .email(account.getEmail())
                .nickname(profile.getNickname())
                .imageUrl(profile.getImageUrl())
                .statusMessage(profile.getStatusMessage())
                .role(account.getRole())
                .build();

        String accessToken = jwtUtil.generateAccessToken(userResponse);
        String refreshToken = jwtUtil.generateRefreshToken(userResponse);

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByAccount(account);

        if (existingToken.isPresent()) {
            // 기존 Refresh Token 업데이트
            RefreshToken refreshTokenEntity = existingToken.get();
            refreshTokenEntity.setToken(refreshToken);
            refreshTokenEntity.setExpirationDate(LocalDateTime.now().plusDays(7));
            refreshTokenRepository.save(refreshTokenEntity);
        } else {
            refreshTokenRepository.save(
                    RefreshToken.builder()
                            .token(refreshToken)
                            .account(account)
                            .expirationDate(LocalDateTime.now().plusDays(7))
                            .build()
            );
        }

        account.setLastLoginAt(LocalDateTime.now());
        accountRepository.save(account);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    /**
     * 고유 닉네임 생성기
     *
     * @param nickname 닉네임
     * @return uniqueNickname
     */
    private String generateUniqueNickname(String nickname) {
        // 닉네임 중복 확인
        String uniqueNickname = nickname;
        Random random = new Random();

        // 중복된 닉네임이 있는지 확인
        while (profileRepository.existsByNickname(uniqueNickname)) {
            // 닉네임 뒤에 랜덤 숫자를 붙여서 새로운 닉네임 생성
            uniqueNickname = nickname + "s" + (100000 + random.nextInt(900000)); // 10만 부터 99만9999 까지 랜덤 숫자
        }

        return uniqueNickname;
    }

}
