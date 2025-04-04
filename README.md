# Some To Do - 일정, 가계부 관리 앱

## 💡 1. 프로젝트 개요

### 📌 프로젝트명  
**SomeToDo** – 일정 및 가계부 관리 통합 앱

### 🎯 목적  
하루의 스케줄과 지출 내역을 한 곳에서 관리할 수 있도록 돕는 통합형 모바일 애플리케이션 개발  
→ 사용자 중심의 **간편한 UI/UX**, **계정 인증 및 보안**, **데이터 저장 및 연동**에 중점을 두어 설계

### 🕒 개발 기간  
**2024. 02. 25 ~ 2024. 03. 28 (약 5주)**

| 주차       | 주요 내용 |
|------------|-----------|
| **1주차** | 주제 선정 및 요구사항 분석, 주요 기능 기획 및 기술 스택 결정 |
| **2~3주차** | 개인별 파트 분담 및 기능별 설계, 화면 UI 초안 제작, 기능 단위 개발 진행 |
| **4주차** | 코드 통합 및 화면 연동, API 연결 테스트, 예외 처리 및 오류 수정 |
| **5주차** | 디버깅 및 성능 개선, AWS EC2를 통한 배포, 프로젝트 문서 정리 및 발표 준비 |

### 🧑‍💻 팀 구성 (총 3인)
| 이름       | 역할               | 주요 담당 기능 |
|------------|--------------------|----------------|
| 엄정현 (본인) | **계정 파트 개발**     | 회원가입, 로그인, JWT 인증, 구글 로그인, 프로필 관리 |
| [백건우](https://github.com/gunwoo100/project_schedule)     | 일정 기능 개발         | 일정 등록, 조회, 수정, 삭제 및 UI 구성 |
| [김지욱](https://github.com/orangetunnelkim/moneyFlow)     | 가계부 기능 개발      | 지출 등록, 통계 화면, 가계부 UI 및 백엔드 로직 |

### 🛠 기술 스택
- **Frontend**: Android (Java), Retrofit2, Glide, SharedPreferences  
- **Backend**: Spring Boot, Spring Security, JPA, PostgreSQL, JWT

---

## ✅ 2. 핵심 기능 요약

이 프로젝트의 계정 파트는 사용자 인증 및 정보 관리를 중심으로 다음과 같은 핵심 기능을 포함합니다. 각 기능은 이미지 또는 gif와 함께 직관적으로 이해할 수 있도록 구성되어 있습니다.

---

### 1️⃣ 로그인 화면 (📱 Android 화면)

- **이메일/비밀번호 또는 구글 계정으로 로그인 가능**
- **입력값 유효성 검사 및 로그인 실패 안내 처리**
- 로그인 성공 시, 자동으로 JWT 토큰 저장

<div align="center">
  <img src="https://github.com/user-attachments/assets/becbf6b5-96e0-4f6c-8c58-77011824a966" alt="로그인 화면" width="300">
</div>

---

### 2️⃣ 회원가입 화면

- **이메일, 비밀번호, 닉네임 입력만으로 간편 가입**
- **입력값 유효성 + 이메일/닉네임 중복 검사 포함**
- 회원가입 성공 시 로그인 화면으로 이동

<div align="center">
  <img src="https://github.com/user-attachments/assets/cb9fcce7-2b97-4190-923e-14f2404e2bf8" alt="회원가입 화면" width="300">
</div>

---

### 3️⃣ 설정 페이지

- **프로필 보기, 수정, 비밀번호 변경, 로그아웃 기능 제공**
- 하단 메뉴의 ‘설정’에서 접근 가능

<div align="center">
  <img src="https://github.com/user-attachments/assets/39edc96b-8091-4f33-beb9-83153d404007" alt="설정 화면" width="300">
</div>

---

### 4️⃣ 프로필 수정 화면

- **닉네임, 상태 메시지, 프로필 이미지 수정 가능**
- 수정 후에는 변경된 내용을 반영한 새로운 Access Token 발급

<div align="center">
  <img src="https://github.com/user-attachments/assets/dd0f74f2-0485-4991-a069-b88364005d1d" alt="프로필 수정 화면" width="300">
</div>

---

### 5️⃣ 구글 로그인 (소셜 로그인)

- **Firebase 없이 Google OAuth2.0으로 구현**
- **idToken을 받아 백엔드에서 검증 및 자동 회원가입 처리**
- 이메일 중복 시 기존 계정으로 로그인 실패 처

<div align="center">
  <img src="https://github.com/user-attachments/assets/ccdccd4a-5486-4c55-b1b8-39bb65c7ce72" alt="구글 로그인 화면" width="300">
</div>

---

### 🔁 참고

- 이 섹션은 **주요 기능의 구조와 흐름을 시각적으로 보여주는 역할**을 합니다.
- 실제 동작 장면을 자세히 보고 싶다면 아래의 **4. 시연 영상** 섹션에서 확인할 수 있습니다.

---

## ✨ 3. 기능 구현

계정 파트에서 구현한 주요 기능들을 실제 흐름에 맞춰 설명하고, 각 기능의 구현 방식을 간단히 소개합니다.

---

### ✅ 회원가입

- **Android**
   - 이메일, 비밀번호, 닉네임을 입력받고 **유효성 검사**를 수행합니다.
   - 입력값 확인 후 `SignupRequest` DTO를 생성하여 서버에 전송합니다.  
   - 서버 응답에 따라 성공 메시지를 출력하거나 실패 알림을 띄웁니다.
  
   ```java
    public void onSignupButtonClick(View view) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String nickname = etNickname.getText().toString().trim();

        // ✅ 유효성 검사 (이메일, 비밀번호, 닉네임) — 생략 가능
        if (isInvalidInput(email, password, nickname)) return;

        SignupRequest signupRequest = new SignupRequest(email, password, password, nickname);
        AuthApiService api = RetrofitClient.getClient().create(AuthApiService.class);
        api.signup(signupRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    finish(); // 로그인 화면으로 이동
                }
            }
            ...
        });
    }
   ```

- **Backend**
   - 이메일 및 닉네임 **중복 검사**
   - 비밀번호 **BCrypt 암호화** 후 저장
   - `Account` / `Profile` 테이블에 분리 저장

    ```java
    if (accountRepository.existsByEmail(request.getEmail()))
       throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
    if (profileRepository.existsByNickname(request.getNickname()))
       throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");

    Account account = Account.builder()
          .email(request.getEmail())
          .password(passwordEncoder.encode(request.getPassword())) // ✅ 암호화
          .build();

    accountRepository.save(account);
    ```
  👉 위 코드 이후에 Profile도 account와 연결해 저장됩니다.

---

### ✅ 로그인

#### 🔹 Android (LoginActivity)

- 사용자가 입력한 **이메일과 비밀번호의 유효성 검사** 후, `LoginRequest` 객체를 생성하여 서버에 전달합니다.
- 응답이 성공적일 경우 `Access Token`과 `Refresh Token`을 `TokenManager`에 저장하고 로그인 성공 처리를 합니다.

    ```java
    if (!email.isEmpty() && !password.isEmpty()) {
        Call<LoginResponse> call = apiService.login(new LoginRequest(email, password));
        call.enqueue(new Callback<>() {
            public void onResponse(...) {
                if (response.isSuccessful()) {
                    loginSuccess(response.body()); // ✅ TokenManager에 저장
                }
            }
        });
    }
    ```

👉 `loginSuccess()` 메서드 내부에서 토큰 저장 및 화면 전환 처리

---

#### 🔹 Backend (LoginService)

1. **이메일이 존재하는지 확인**
2. **비밀번호 일치 여부 확인**
3. 성공 시 `Access Token`과 `Refresh Token`을 생성하고 응답

    ```java
    Account account = accountRepository.findByEmail(request.getEmail())
       .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호 오류"));

    if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
       throw new IllegalArgumentException("이메일 또는 비밀번호 오류");
    }
    ```

👉 이후 아래와 같이 토큰을 생성 및 저장합니다:

    ```java
    String accessToken = jwtUtil.generateAccessToken(userResponse);
    String refreshToken = jwtUtil.generateRefreshToken(userResponse);

    // 기존 RefreshToken 갱신 or 새로 저장
    Optional<RefreshToken> existing = refreshTokenRepository.findByAccount(account);
    existing.ifPresentOrElse(
       token -> {
          token.setToken(refreshToken);
          token.setExpirationDate(...);
          refreshTokenRepository.save(token);
       },
       () -> refreshTokenRepository.save(new RefreshToken(...))
    );
    ```

---

### ✅ JWT 토큰 관리

> JWT 기반 인증 시스템은 **토큰을 이용한 무상태(stateless) 인증 방식**입니다. 이 프로젝트에서는 Android에서는 Interceptor로, Spring Boot에서는 JwtUtil 클래스를 통해 각각 토큰을 주고받고 관리하는 구조를 갖습니다

---

#### 📱 **Interceptor (Android)**  
**`AuthInterceptor`** 클래스는 모든 HTTP 요청에 대해 다음과 같은 역할을 합니다:

- 1. **Authorization 헤더 자동 추가**  
   사용자가 로그인할 때 발급받은 Access Token을 SharedPreferences에서 꺼내와, 매 요청마다 `Authorization: Bearer <Access Token>` 형식으로 요청 헤더에 자동으로 추가합니다. 이로써 API 요청마다 토큰을 수동으로 붙이지 않아도 됩니다.

- 2. **Access Token 만료 시 자동 갱신 처리**  
   서버로부터 `401 Unauthorized` 또는 `403 Forbidden` 응답이 돌아오면, Access Token이 만료되었거나 잘못되었음을 의미합니다.  
   이 경우 `Refresh Token`을 이용해 새로운 Access Token을 받아오는 요청을 자동으로 실행합니다.

- 3. **토큰 갱신 실패 시 로그아웃 처리**  
   Refresh Token이 만료되었거나 오류가 발생하면, 저장된 토큰을 모두 삭제하고 로그인 화면으로 이동시킵니다.  
   사용자는 자동으로 로그아웃되어 다시 로그인해야 합니다.

📌 **정리하면**: 사용자는 로그인 이후 별도의 인증 과정을 느끼지 않고 앱을 사용할 수 있으며, Interceptor는 인증을 자동화하여 사용자 경험을 향상시킵니다.

   ```java
   @Override
   public Response intercept(@NonNull Chain chain) throws IOException {
      String accessToken = TokenManager.getAccessToken();
      Request original = chain.request();

      // Access Token 헤더에 자동 추가
      Request request = original.newBuilder()
         .header("Authorization", "Bearer " + accessToken)
         .build();

      Response response = chain.proceed(request);

      // 토큰 만료 응답 처리 (401, 403)
      if (response.code() == 401 || response.code() == 403) {
         String refreshToken = TokenManager.getRefreshToken();
         String newAccessToken = TokenManager.refreshAccessToken(refreshToken);

         if (newAccessToken != null) {
            TokenManager.setAccessToken(newAccessToken);
            Request newRequest = original.newBuilder()
               .header("Authorization", "Bearer " + newAccessToken)
               .build();
            return chain.proceed(newRequest);
         } else {
            forceLogout(); // 로그아웃 처리
         }
      }

      return response;
   }
   ```

#### 🌐 **JwtUtil (Backend)**  
**`JwtUtil`** 클래스는 백엔드(Spring Boot)에서 JWT 토큰을 생성하고 검증하는 핵심 클래스입니다.

- 1. **Access Token & Refresh Token 생성**
   - `generateAccessToken(UserResponse userResponse)`  
     사용자의 기본 정보를 바탕으로 JWT 토큰을 생성합니다.  
     이 Access Token은 일반적인 API 요청에 사용되며 만료 시간이 짧습니다(보통 1시간).

   - `generateRefreshToken(UserResponse userResponse)`  
     Access Token이 만료되었을 때 새 토큰을 발급받기 위해 사용되는 장기 토큰입니다(보통 7일).

- 2. **토큰 정보 파싱 및 유효성 검사**
   - `extractClaims(String token)`  
     JWT에 담긴 클레임(이메일, 닉네임 등 사용자 정보)을 추출합니다.
     
   - `extractEmail(String token)`  
     토큰에서 이메일(subject)을 쉽게 가져올 수 있도록 추출 메서드 제공.

   - `validateToken(String token)`  
     토큰의 만료 여부를 판단하여, 서버가 해당 토큰을 받아들일지 결정합니다.

📌 **정리하면**: JwtUtil은 인증에 필요한 토큰 생성 및 검증을 담당하며, Spring Security와 연결되어 실질적인 인증 흐름의 중심이 됩니다.

   ```java
   public String generateAccessToken(UserResponse userResponse) {
      return Jwts.builder()
         .setSubject(userResponse.getEmail()) // 이메일을 subject로 설정
         .claim("nickname", userResponse.getNickname())
         // 기타 클레임 설정 ...
         .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME)) // 만료 시간 설정
         .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256) // 서명 알고리즘 설정
         .compact();
   }

   public String generateRefreshToken(UserResponse userResponse) {
      return Jwts.builder()
         .setSubject(userResponse.getEmail())
         .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
         .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
         .compact();
   }

   public Claims extractClaims(String token) {
      return Jwts.parserBuilder()
         .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
         .build()
         .parseClaimsJws(token)
         .getBody();
   }

   public String extractEmail(String token) {
      return extractClaims(token).getSubject();
   } 

   public boolean validateToken(String token) {
      return extractClaims(token).getExpiration().after(new Date());
   }
   ```

---

### ✅ 구글 로그인

- Android에서 구글 계정 선택 후 `idToken`을 획득
- 해당 토큰을 서버로 전송
- 서버에서는 구글 API로 `idToken`을 검증하고,
   - 신규 유저인 경우 회원가입 처리
   - 기존 유저인 경우 로그인 처리

   ```java
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
   ```

---

### ✅ 프로필 수정

#### 📱 **Android**
- 설정 화면에서 닉네임, 이미지, 상태 메시지를 수정
- 변경된 정보를 `ProfileEditRequest` 형태로 서버에 전송  
→ 응답으로 **새로운 Access Token**을 받아 앱에 즉시 반영

#### 🌐 **Backend**

**컨트롤러 처리 흐름**

- 로그인한 사용자 정보(@AuthenticationPrincipal)에서 이메일을 추출
- 유효성 검사 후 `profileService.update()` 호출
- 응답으로 새로운 Access Token 반환

```java
@PatchMapping
public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileEditRequest request, 
                                       @AuthenticationPrincipal UserPrincipal user) {
   if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
    
   String email = user.getEmail();
   Map<String, String> token = profileService.update(request, email);
   return ResponseEntity.ok(token);
}
```

**서비스 처리 흐름**

- 이메일 기반으로 Account / Profile 조회
- 닉네임 중복 여부 확인
- 프로필 정보 수정 및 저장
- 변경된 사용자 정보를 기반으로 **새로운 Access Token 생성** 후 반환

---

📌 **정리하면**  
→ 프로필 수정 시 새로운 Access Token을 즉시 발급하여 변경사항을 반영  
→ 서버에서 닉네임 중복, 인증 여부까지 **이중 확인**하여 안정성을 확보합니다.

---

## 🎬 4. 시연 영상

- 초기 로그인 화면 → 회원가입 화면 전환
- 이메일/닉네임 입력(유효성 검사) → 가입 완료 후 로그인 화면으로 전환
- 로그인 후 메인 화면 진입
- 설정 → 프로필 수정 동작 → 비밀번호 변경 → 로그아웃
- 바뀐 비밀번호로 재 로그인

<div align="center">
  <a href="https://www.youtube.com/shorts/bZ9ycIyjW48" target="_blank">
    <img src="https://img.youtube.com/vi/bZ9ycIyjW48/0.jpg" alt="Sometodo 시연 영상" width="300">
  </a>
  <p>🎬 <a href="https://www.youtube.com/shorts/bZ9ycIyjW48" target="_blank">시연 영상 보러가기</a></p>
</div>

---

## 🧠 5. 추가 중요 코드 설명

### 🔐 TokenManager (Android)

- 토큰을 `SharedPreferences`에 저장 및 관리합니다.
- 자동 로그인, 로그아웃, 토큰 갱신 등을 앱 전반에서 처리하는 핵심 클래스입니다.
- Interceptor와 연결되어 Access Token을 불러오거나 갱신된 토큰을 저장합니다.

```java
public static void saveTokens(String accessToken, String refreshToken) {
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString("access_token", accessToken);
    editor.putString("refresh_token", refreshToken);
    editor.apply();
}

public static String refreshAccessToken(String refreshToken) {
    try {
        AuthApiService apiService = RetrofitClient.getClient().create(AuthApiService.class);
        Response<RefreshResponse> response = apiService.refresh(new RefreshRequest(refreshToken)).execute();
        if (response.isSuccessful() && response.body() != null) {
            return response.body().getAccessToken();
        }
    } catch (IOException e) {
        Log.e("TokenManager", "Token refresh failed", e);
    }
    return null;
}
```

---

### 🛡️ JwtAuthenticationFilter (Backend)

- JWT 유효성을 검사하고, 인증된 사용자 정보를 Spring Security 인증 컨텍스트에 주입하는 역할을 합니다.
- 유효한 토큰인 경우, 사용자 정보를 `UserPrincipal`로 변환하고 `SecurityContextHolder`에 설정합니다.

```java
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    String token = resolveToken(request);
    if (token != null && jwtUtil.validateToken(token)) {
        String email = jwtUtil.extractEmail(token);
        Claims claims = jwtUtil.extractClaims(token);
        UserResponse userResponse = new UserResponse(email, ...);
        UserPrincipal userPrincipal = new UserPrincipal(userResponse);
        Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, token, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    filterChain.doFilter(request, response);
}
```

---

### 🛡️ SecurityConfig (Spring Security 보안 설정)

이 클래스는 Spring Security에서 제공하는 보안 설정을 구성하는 곳으로, **JWT 기반 인증 시스템에 맞게 커스터마이징**되어 있습니다.

#### 📌 주요 설명

- **@EnableWebSecurity**: Spring Security를 활성화시키는 어노테이션입니다.
- **@Configuration**: 설정 클래스로 등록합니다.
- **jwtAuthenticationFilter**: 사용자가 보낸 JWT를 검사하는 필터로, 인증 과정에서 가장 핵심적인 역할을 합니다.

---

#### 🔑 핵심 설정 항목

1. **Whitelist (비인증 경로 설정)**
   ```java
   private static final String[] WHITELIST = {
       "/api/auth/signup",
       "/api/auth/login",
       "/api/auth/google/login",
       "/api/auth/refresh",
   };
   ```
   - 위에 정의된 경로들은 인증 없이 접근할 수 있도록 허용됩니다.
   - 회원가입, 로그인, 토큰 갱신 등 인증 전 과정에 필요한 URL입니다.

2. **CSRF 비활성화**
   ```java
   http.csrf(AbstractHttpConfigurer::disable)
   ```
   - JWT는 세션 기반이 아닌 **무상태(stateless)** 인증 방식이므로 CSRF 보호는 필요하지 않습니다.

3. **세션 사용 안 함**
   ```java
   session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
   ```
   - 서버가 세션을 유지하지 않도록 설정합니다. 모든 인증은 토큰을 기반으로 처리됩니다.

4. **요청 권한 설정**
   ```java
   .authorizeHttpRequests(auth -> auth
       .requestMatchers(WHITELIST).permitAll()
       .anyRequest().authenticated()
   )
   ```
   - 화이트리스트 외의 모든 요청은 인증된 사용자만 접근 가능하게 합니다.

5. **JWT 필터 등록**
   ```java
   .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
   ```
   - 기본 Username/Password 인증 필터 전에 **커스텀 JWT 필터**를 삽입하여 JWT 인증을 먼저 처리합니다.

---

### ✅ 요약 정리

- `SecurityConfig`는 애플리케이션의 **전체 보안 흐름을 제어**하는 설정 클래스입니다.
- 화이트리스트 경로만 인증 없이 접근 가능하며, 그 외 모든 API는 JWT 토큰이 있어야 접근 가능합니다.
- 세션을 사용하지 않고 토큰 기반으로 인증이 이루어지며, 커스텀 JWT 인증 필터(`JwtAuthenticationFilter`)를 통해 토큰을 검증합니다.

---
