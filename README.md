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

![image](https://github.com/user-attachments/assets/becbf6b5-96e0-4f6c-8c58-77011824a966)

---

### 2️⃣ 회원가입 화면

- **이메일, 비밀번호, 닉네임 입력만으로 간편 가입**
- **입력값 유효성 + 이메일/닉네임 중복 검사 포함**
- 회원가입 성공 시 로그인 화면으로 이동

![image](https://github.com/user-attachments/assets/cb9fcce7-2b97-4190-923e-14f2404e2bf8)

---

### 3️⃣ 설정 페이지

- **프로필 보기, 수정, 비밀번호 변경, 로그아웃 기능 제공**
- 하단 메뉴의 ‘설정’에서 접근 가능

![image](https://github.com/user-attachments/assets/39edc96b-8091-4f33-beb9-83153d404007)

---

### 4️⃣ 프로필 수정 화면

- **닉네임, 상태 메시지, 프로필 이미지 수정 가능**
- 수정 후에는 변경된 내용을 반영한 새로운 Access Token 발급

![image](https://github.com/user-attachments/assets/dd0f74f2-0485-4991-a069-b88364005d1d)

---

### 5️⃣ 구글 로그인 (소셜 로그인)

- **Firebase 없이 Google OAuth2.0으로 구현**
- **idToken을 받아 백엔드에서 검증 및 자동 회원가입 처리**
- 이메일 중복 시 기존 계정으로 로그인 실패 처

![image](https://github.com/user-attachments/assets/495fe2e1-975a-444d-b1d3-395e91333d2d)

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

