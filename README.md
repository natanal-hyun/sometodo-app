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
  <img src="https://github.com/user-attachments/assets/495fe2e1-975a-444d-b1d3-395e91333d2d" alt="구글 로그인 화면" width="300">
</div>
![image](https://github.com/user-attachments/assets/df971de6-a757-46ae-b2c9-a5af3b654c49)

---

### 🔁 참고

- 이 섹션은 **주요 기능의 구조와 흐름을 시각적으로 보여주는 역할**을 합니다.
- 실제 동작 장면을 자세히 보고 싶다면 아래의 **4. 시연 영상** 섹션에서 확인할 수 있습니다.

---

## 3. 기능 구현

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
        String passwordCheck = etPasswordCheck.getText().toString().trim();
        String nickname = etNickname.getText().toString().trim();

        // 1️⃣ 입력값 검증
        if (email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty() || nickname.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "올바른 이메일 형식을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passwordCheck)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8 || password.length() > 20) {
            Toast.makeText(this, "비밀번호는 8자 이상 20자 이하이어야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nickname.matches("^[a-zA-Z0-9가-힣]+$")) {
            Toast.makeText(this, "닉네임은 영문, 숫자, 한글만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nickname.length() < 2 || nickname.length() > 12) {
            Toast.makeText(this, "닉네임은 2자 이상 12자 이하이어야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2️⃣ API 요청을 위한 SignupRequest 객체 생성
        SignupRequest signupRequest = new SignupRequest(email, password, passwordCheck, nickname);

        // 3️⃣ Retrofit을 통해 회원가입 요청
        AuthApiService authApiService = RetrofitClient.getClient().create(AuthApiService.class);
        Call<Void> call = authApiService.signup(signupRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();

                    // 4️⃣ 성공 시 로그인 화면으로 이동
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "회원가입 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(SignupActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }   
   ```

- **Backend**
   - 이메일 및 닉네임 **중복 검사**
   - 비밀번호 **BCrypt 암호화** 후 저장
   - `Account` / `Profile` 테이블에 분리 저장
  
   ```java
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
   ```

---

### ✅ 로그인

- **Android**
   - 사용자가 입력한 이메일과 비밀번호로 서버에 요청
   - 로그인 성공 시 **Access Token**과 **Refresh Token**을 발급받아 `TokenManager`에 저장

   ```java
   public void onLoginButtonClick(View view) {
      String email = etEmail.getText().toString().trim();
      String password = etPassword.getText().toString().trim();

      if (email.isEmpty() || password.isEmpty()) {
         Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
         return;
      }

      if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
         Toast.makeText(this, "올바른 이메일 형식을 입력하세요.", Toast.LENGTH_SHORT).show();
         return;
      }

      AuthApiService apiService = RetrofitClient.getClient().create(AuthApiService.class);
      Call<LoginResponse> call = apiService.login(new LoginRequest(email, password));
      
      call.enqueue(new Callback<LoginResponse>() {
         @Override
         public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
               loginSuccess(response.body());
            } else {
               try (ResponseBody errorBody = response.errorBody()) {  // ✅ try-with-resources 사용
                  if (errorBody != null) {
                     Toast.makeText(LoginActivity.this, "로그인 실패: " + errorBody.string(), Toast.LENGTH_SHORT).show();
                  } else {
                     Toast.makeText(LoginActivity.this, "로그인 실패: 알 수 없는 오류", Toast.LENGTH_SHORT).show();
                  }
               } catch (Exception e) {
                  Toast.makeText(LoginActivity.this, "로그인 실패: 응답 처리 오류", Toast.LENGTH_SHORT).show();
               }
            }
         }
         
         @Override
         public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
            Toast.makeText(LoginActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
         }
      });
   }
   ```

- **Backend**
   - 이메일 존재 여부 확인 후, 비밀번호 검증
   - 검증 성공 시 JWT 토큰 생성 및 응답
  
   ```java
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
   ```

   ```java
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
   public class AuthInterceptor implements Interceptor {
      private final Context context;

      public AuthInterceptor(Context context) {
         this.context = context.getApplicationContext(); // ApplicationContext 사용
      } 
      
      @NonNull
      @Override
      public Response intercept(@NonNull Chain chain) throws IOException {
         String accessToken = TokenManager.getAccessToken();
         Request original = chain.request();
         
         // refresh 엔드포인트는 토큰 검증 건너뛰기
         if (original.url().encodedPath().contains("/api/auth/refresh")) {
            return chain.proceed(original);
         }   
         
         Request request = original.newBuilder()
            .header("Authorization", "Bearer " + accessToken)
            .build();

         Response response = chain.proceed(request);

         // 401 Unauthorized → Access Token이 만료되었을 가능성이 있음
         if (response.code() == 401 || response.code() == 403) {
            synchronized (this) {
               String refreshToken = TokenManager.getRefreshToken();
               if (refreshToken != null) {
                  String newAccessToken = TokenManager.refreshAccessToken(refreshToken);
                  if (newAccessToken != null) {
                     TokenManager.setAccessToken(newAccessToken);
                     
                     // 새로운 Access Token으로 다시 요청 실행
                     Request newRequest = original.newBuilder()
                        .header("Authorization", "Bearer " + newAccessToken)
                        .build();
                     return chain.proceed(newRequest);
                  } else {
                     // 토큰 갱신 실패: 강제 로그아웃 처리
                     forceLogout();
                  }
               } else {
                  // Refresh Token이 없으면 강제 로그아웃
                  forceLogout();
               }
            }
         }   
         
         return response;
         }

      /**
      * 강제로 로그아웃 이벤트를 발생시켜 앱 전체를 로그아웃 상태로 만듭니다.
      */
      private void forceLogout() {
         // 토큰 삭제
         TokenManager.clearTokens();

         // UI 스레드에서 실행
         new Handler(Looper.getMainLooper()).post(() -> {
            Intent intent = new Intent(context, LoginActivity.class);
            // 기존의 모든 액티비티를 제거하고 새로운 태스크에서 LoginActivity 실행
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
         });
      }
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
   @Component
   public class JwtUtil {
      private static final String SECRET_KEY = "F2...";
      private static final long ACCESS_EXPIRATION_TIME = 1000 * 10 * 60 * 60; // 1시간
      private static final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

      public String generateAccessToken(UserResponse userResponse) {
         return Jwts.builder()
            .setSubject(userResponse.getEmail()) // 이메일을 subject로 설정
            .claim("nickname", userResponse.getNickname())
            .claim("imageUrl", userResponse.getImageUrl())
            .claim("statusMessage", userResponse.getStatusMessage())
            .claim("role", userResponse.getRole().toString())
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

- **Android**
   - 설정 화면에서 닉네임, 이미지, 상태 메시지 수정
   - 변경된 값을 서버에 요청 

- **Backend**
   - 인증된 사용자의 이메일로 `Account`와 `Profile` 조회
   - 수정된 값 저장 후 새로운 Access Token 반환 (즉시 반영 목적)

   ```java
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
   ```

   ```java
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
   ```

---
