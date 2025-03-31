# Some To Do (일정 및 가계부 관리 앱)

## Backend 개발 노트

## 📜 목차

### 백엔드 (Spring Boot)
- [Spring Boot Start](#spring-boot-start)
- [User Entity 설계](#user-entity-설계)
- [유효성 검사](#유효성-검사)
- [User 네이밍 이슈](#user-네이밍-이슈)
- [H2 Database 및 JPA 설정](#h2-database-및-jpa-설정)
- [Backend 회원가입](#backend-회원가입)
- [비밀번호 인코딩](#비밀번호-인코딩)
- [Backend 로그인](#backend-로그인)
- [JWT 기반 로그인 설계](#jwt-기반-로그인-설계)
- [Refresh Token 설계](#refresh-token-설계)
- [Refresh Token을 이용한 Access Token 갱신](#refresh-token을-이용한-access-token-갱신)
- [로그인 요청 시 403 Forbidden 오류 발생](#로그인-요청-시-403-forbidden-오류-발생)
- [프로필 수정 API 구현](#프로필-수정-api-구현)
- [비밀번호 변경 API 구현](#비밀번호-변경-api-구현) 
- [Postgre SQL 연동](#postgre-sql-연동)
- [구글 로그인 연동](#구글-로그인-연동)

---

## Spring Boot Start

<img src="./img/start_spring.png" /> <br />

### 📌 Spring Boot 프로젝트 의존성 설명 (역할 / 특징 / 이점)

#### 1️⃣ Lombok (Developer Tools)
- 역할: 보일러플레이트 코드(반복적인 코드) 줄이기
- 특징: **@Getter, @Setter, @ToString, @Builder** 등의 애노테이션 제공
- 이점: 코드 가독성 증가, 유지보수 편리, 개발 속도 향상

#### 2️⃣ Spring Web
- 역할: 웹 애플리케이션 및 **REST API** 개발
- 특징: Spring MVC 기반, 내장 톰캣 서버 포함
- 이점: 웹 서버 설정 없이 바로 실행 가능, RESTful API 개발 용이

#### 3️⃣ Mustache (Template Engine)
- 역할: 서버 사이드 HTML 템플릿 엔진
- 특징: {{title}}, {{content}} 같은 간결한 문법 사용
- 이점: 가벼움, HTML과 Java 코드 분리 가능, 유지보수 용이

#### 4️⃣ Spring Data JPA
- 역할: 객체-데이터베이스 매핑(ORM) 및 **CRUD** 자동화
- 특징: SQL 대신 메서드 (findById(), save(), delete()) 사용 가능
- 이점: SQL 작성 필요 없음, 데이터베이스 변경 시 유지보수 용이

#### 5️⃣ H2 Database
- 역할: **개발 및 테스트**용 인메모리 데이터베이스
- 특징: 애플리케이션 실행 시 자동 생성 및 초기화
- 이점: 빠른 개발 및 테스트 가능, 별도 DB 설정 불필요

#### 6️⃣ PostgreSQL Driver
- 역할: **PostgreSQL**과 연결하는 JDBC 드라이버
- 특징: Spring Data JPA와 함께 사용 가능
- 이점: 실제 서비스 환경에서 안정적인 데이터베이스 사용 가능

#### ✅ PostgreSQL을 바로 연결하는 것이 나을까?
- 초기 개발: H2 사용 → 빠른 개발 및 테스트
- 본격 개발 & 배포: PostgreSQL 사용 → 실전 환경과 동일한 DB로 전환 🚀

---

## User Entity 설계

### 📌 개발 노트 - User 엔티티 정의

- 1️⃣ 개요
> User 엔티티는 **회원 정보**를 관리하는 핵심 모델이며, 인증 및 권한 부여, 상태 관리 등의 기능을 담당한다.
회원 가입, 로그인, 프로필 관리 등에서 사용되며, Spring Security 및 JWT 기반의 인증 시스템과 연계된다.

- 2️⃣ User 엔티티 코드
```java
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키 (PK)

    @Column(nullable = false, unique = true)
    private String email; // 이메일 (로그인 ID 역할)

    @Column(nullable = false)
    private String password; // 비밀번호 (암호화 필요)

    @Column(nullable = false, unique = true)
    private String nickname; // 닉네임 (중복 방지)

    @Column
    private String imageUrl; // 프로필 이미지 URL

    @Column(length = 100)
    private String statusMessage; // 상태 메시지 (한 줄 소개)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER; // 유저 권한 (USER, MANAGER, ADMIN, SUPER_ADMIN)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE; // 계정 상태 (ACTIVE, BANNED, DEACTIVATED)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider; // 로그인 방식 (GOOGLE, EMAIL 등)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 가입일

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now(); // 마지막 수정일

    @Column
    private LocalDateTime lastLoginAt; // 마지막 로그인 시간

    @Column(unique = true)
    private String oauthId; // OAuth 로그인 사용자의 ID (ex. Google ID)
}
```

- 3️⃣ 필드 설명
| 필드명 | 타입 | 설명 |
|---|---|---|
|id|            Long|	기본 키 (PK), 자동 증가|
|email| 	    String|	이메일, 로그인 ID 역할 (중복 방지)|
|password|	    String|	비밀번호 (암호화 필요)|
|nickname|	    String|	닉네임 (중복 방지)|
|imageUrl|      String| 프로필 이미지 URL|
|statusMessage|	String|	사용자 상태 메시지 (짧은 소개글)|
|role|	        Enum|   사용자 역할 (USER, MANAGER, ADMIN, SUPER_ADMIN)|
|status|	    Enum|	계정 상태 (ACTIVE, BANNED, DEACTIVATED, DELETED)|
|authProvider|	Enum|	로그인 방식 (GOOGLE, EMAIL 등)|
|createdAt|	    LocalDateTime|	가입일 (최초 생성)|
|updatedAt|	    LocalDateTime|	마지막 수정일|
|lastLoginAt|	LocalDateTime|	마지막 로그인 시간|
|oauthId|	    String|	        OAuth 로그인 사용자의 ID|

- 4️⃣ Enum 클래스 정의
``` java
public enum UserRole {
    USER,         // 일반 사용자
    MANAGER,      // 제한적인 관리자 권한
    ADMIN,        // 관리자
    SUPER_ADMIN   // 최상위 관리자 (모든 권한 보유)
}

public enum UserStatus {
    ACTIVE,       // 활성 계정
    BANNED,       // 정지된 계정
    DEACTIVATED,  // 탈퇴된 계정
    DELETED       // 완전 삭제 (소프트 삭제)
}

public enum AuthProvider {
    EMAIL,        // 이메일 가입
    GOOGLE        // 구글 로그인
}
```

---

## 유효성 검사

### 📌 유효성 검사 (Validation) - 회원가입 요청

- 1️⃣ SignupRequest DTO
> 회원가입 시 클라이언트에서 전달받는 데이터를 담는 DTO (Data Transfer Object) 입니다.
```java
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
    private String passwordCheck;
}
```

- 2️⃣ 구성 요소
|필드명|타입|설명|
|---|---|---|
|email|	String|	사용자의 이메일 (로그인 ID 역할)|
|password|	String|	사용자 비밀번호|
|passwordCheck|	String|	비밀번호 확인 (일치 여부 검증)|

- 3️⃣ 유효성 검사 적용
> 아래와 같이 Validation을 추가하여 유효성을 검사할 수 있습니다.
```java
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.") 
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordCheck;

    @AssertTrue(message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
    public boolean isPasswordMatch() {
        return password != null && password.equals(passwordCheck);
    }
}
```

- 4️⃣ 유효성 검사 설명
    - @Email : 이메일 형식을 검증
    - @NotBlank : 공백 입력 방지
    - @Size(min = 8, max = 20) : 비밀번호 길이 설정 (8자 이상 20자 이하)
    - @AssertTrue : 반드시 true를 반환해야만 검증을 통과
    
- 5️⃣ 서버에서 유효성 검사 예제
> Controller에서 유효성 검사를 수행할 때, @Valid 어노테이션을 사용하여 자동 검증합니다.
```java
@PostMapping("/signup")
public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request, BindingResult result) {
    if (result.hasErrors()) {
        return ResponseEntity.badRequest().body(result.getAllErrors().get(0).getDefaultMessage());
    }
    return ResponseEntity.ok("회원가입 성공!");
}
```

---

## User 네이밍 이슈

### 📌 User 테이블 네이밍 이슈 및 해결

- 1. ⚠️ 발생한 문제
> Spring Boot 프로젝트에서 User 엔터티를 사용하여 JPA를 활용하려 했으나, H2 Database에서 예약어(USER)와 충돌하는 문제가 발생함.
    - 오류 메시지:
    ```sql
    Caused by: org.h2.jdbc.JdbcSQLSyntaxErrorException: 
    Syntax error in SQL statement "drop table if exists [*]user cascade"; expected "identifier";
    ```

- 2. 🔍 해결 방법
    - 1️⃣ Entity에 Table 명명 추가
    > @Table(name = "users") 어노테이션을 추가하여 테이블명을 변경하여 충돌 방지
    ```java
    @Entity
    @Table(name = "users")
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String username;
        private String password;
        // 기타 필드
    }
    ```
    
    - 2️⃣ User Entity 분리 ✅ 채택
    > User 를 **Account와 Profile** 두 개의 테이블로 분리하여 네이밍 충돌을 방지
        - 네이밍 충돌 방지 및 유지보수성과 확장성 고려
        - 기존 User 테이블을 역할별로 분리하여 각각의 책임을 명확히 함
    > 변경 내용:
        - Account 테이블 → **계정 정보**(email, password, 권한, 가입일, 인증 방식 등) 저장
        - Profile 테이블 → **사용자 프로필 정보**(nickname, 상태 메시지, 프로필 이미지 등) 저장
        

- 3. 📌 정리
    - 기존 User 테이블을 Account & Profile로 분리하여 해결
    - **Account**는 **로그인 관련 정보**, **Profile**은 **사용자 프로필 정보** 저장
    - 예약어 충돌을 방지하면서, 유지보수성과 확장성을 고려한 구조로 개선
    - ✅ 향후 추가적인 기능 개발 시, Profile 테이블에 더 많은 사용자 정보를 추가 가능





---

## H2 Database 및 JPA 설정
> Spring Boot 프로젝트에서 H2 Database를 설정하고 JPA를 적용하려면, application.properties 파일에 적절한 설정을 추가해야 합니다. 아래는 각 설정 항목에 대한 설명입니다.

### 🔹 H2 Database란?
> H2 Database는 **경량의 관계형 데이터베이스(RDBMS)** 로, 빠른 실행 속도와 내장형 데이터베이스 기능을 제공하여 개발 및 테스트 환경에서 자주 사용됩니다. 메모리(Memory) 모드로 실행할 경우, 애플리케이션 종료 시 데이터가 삭제되지만, 파일(File) 모드로 실행하면 데이터를 영구적으로 저장할 수도 있습니다.

### 🔹 JPA(Java Persistence API)란?
> JPA는 자바 객체(Object)와 관계형 데이터베이스(Relational Database) 간의 매핑을 쉽게 해주는 인터페이스입니다. Hibernate와 같은 구현체를 활용하여 SQL을 직접 작성하지 않고도 데이터베이스 조작이 가능하며, 객체지향적인 데이터 관리가 가능합니다.

### 🔹 1. H2 데이터베이스 설정
```property
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.platform=h2
```

### ✅ 설명:
- spring.datasource.url=jdbc:h2:mem:testdb → H2 Database를 메모리(Memory) 모드로 실행 (서버 종료 시 데이터 삭제)
- spring.datasource.driver-class-name=org.h2.Driver → H2 Database 드라이버 지정
- spring.datasource.username=sa → 기본 사용자 ID 설정 (sa는 기본값)
- spring.datasource.password= → 비밀번호 설정 비워둠
- spring.datasource.platform=h2 → H2를 사용하는 데이터베이스 플랫폼 지정

### 🔹 2. H2 Console 활성화
```property
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### ✅ 설명:
- spring.h2.console.enabled=true → H2 Console을 활성화
- spring.h2.console.path=/h2-console → H2 Console의 접근 경로 설정
- 브라우저에서 http://localhost:8080/h2-console 접속 가능

### 🔹 3. JPA 설정
```property
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

### ✅ 설명:
- spring.jpa.database-platform=org.hibernate.dialect.H2Dialect → H2 Database용 Hibernate Dialect 설정
- spring.jpa.hibernate.ddl-auto=update → 테이블을 자동 업데이트 (테스트 환경에서 유용)

---

## Backend 회원가입

### 1️⃣ 회원가입 기능 개요
- 사용자가 이메일, 비밀번호, 닉네임을 입력하여 계정을 생성하는 기능
- 이메일 및 닉네임 중복 검사를 수행하여 중복된 계정 생성 방지

### 2️⃣ 📌 회원가입 요청 DTO (`SignupRequest`)
회원가입 시 클라이언트가 전송하는 데이터 구조를 정의하는 DTO입니다.
```java
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String passwordCheck;
    
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 12, message = "닉네임은 2자 이상 12자 이하이어야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "닉네임은 특수문자 없이 영문, 숫자, 한글만 사용할 수 있습니다.")
    private String nickname;

    @AssertTrue(message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
    public boolean isPasswordMatch() {
        return password != null && password.equals(passwordCheck);
    }
}
```

### 3️⃣ 📌 Account 엔티티 정의
사용자의 계정 정보를 저장하는 엔티티입니다.
```java
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키 (PK)

    @Column(nullable = false, unique = true)
    private String email; // 이메일 (로그인 ID 역할)

    @Column(nullable = false)
    private String password; // 비밀번호 (암호화 필요)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER; // 유저 권한 (기본값: USER)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE; // 계정 상태 (ACTIVE, BANNED, DEACTIVATED, DELETED)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider; // 로그인 방식 (GOOGLE, EMAIL 등)

    @Column(unique = true)
    private String oauthId; // OAuth 로그인 사용자의 ID (ex. Google ID)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 가입일

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now(); // 마지막 수정일

    @Column
    private LocalDateTime lastLoginAt; // 마지막 로그인 시간
}
```

### 4️⃣ 📌 Profile 엔티티 정의
사용자의 프로필 정보를 저장하는 엔티티입니다.
```java
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 기본 키 (PK)

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account; // Account와 1:1 관계

    @Column(nullable = false, unique = true)
    private String nickname; // 닉네임 (중복 방지)

    @Column
    private String imageUrl; // 프로필 이미지 URL

    @Column(length = 100)
    private String statusMessage; // 상태 메시지 (한 줄 소개)

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now(); // 마지막 수정일
}
```

### 5️⃣ 📌 Repository (AccountRepository & ProfileRepository)
사용자의 이메일과 닉네임 중복 여부를 확인하는 메서드를 포함한 JPA Repository입니다.
```java
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
}

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByNickname(String nickname);
}
```

### 6️⃣ 📌 회원가입 서비스 (AuthService)
회원가입 비즈니스 로직을 처리합니다.
```java
@Service
public class AuthService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProfileRepository profileRepository;

    /**
     * 회원가입 처리
     *
     * @param request 회원가입 요청 DTO
     * @throws IllegalArgumentException 이메일 중복 시 예외 발생
     */
    public void registerAccount(@Valid SignupRequest request) {
        // 이메일 중복 체크
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Account account = Account.builder()
                .email(request.getEmail())
                .password(request.getPassword())
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
}
```

### 7️⃣ 📌 회원가입 컨트롤러 (AuthApiController)
클라이언트가 회원가입을 요청하는 엔드포인트를 제공하는 컨트롤러입니다.
```java
/**
 * 인증(Auth) 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    AuthService authService;

    /**
     * 회원가입 API
     *
     * @RequestBody SignupRequest 회원가입 요청 데이터 (DTO)
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
}
```

### 8️⃣ 📌 API 테스트 (닉네임 추가 반영)

#### 📌 **회원가입 요청 (POST /api/auth/signup - 성공 케이스)**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "passwordCheck": "password123",
  "nickname": "testuser"
}
```
#### ✅ **성공 응답 (200 OK)**
```json
{
  "message": "회원가입 성공!"
}
```

#### 📌 **회원가입 요청 (POST /api/auth/signup - 실패 케이스, 비밀번호 불일치)**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "passwordCheck": "password456",
  "nickname": "testuser"
}
```
#### ✅ **실패 응답 (400 Bad Request)**
```json
{
  "message": "비밀번호와 비밀번호 확인이 일치하지 않습니다."
}
```

#### 📌 **회원가입 요청 (POST /api/auth/signup - 실패 케이스, 잘못된 이메일 형식)**
```json
{
  "email": "invalid-email",
  "password": "password123",
  "passwordCheck": "password123",
  "nickname": "testuser"
}
```
#### ✅ **실패 응답 (400 Bad Request)**
```json
{
  "message": "올바른 이메일 형식이 아닙니다."
}
```

#### 📌 **회원가입 요청 (POST /api/auth/signup - 실패 케이스, 닉네임 중복)**
```json
{
  "email": "newuser@example.com",
  "password": "password123",
  "passwordCheck": "password123",
  "nickname": "testuser"
}
```
#### ✅ **실패 응답 (400 Bad Request)**
```json
{
  "message": "이미 사용 중인 닉네임입니다."
}
```

#### 📌 **회원가입 요청 (POST /api/auth/signup - 실패 케이스, 닉네임 길이 초과)**
```json
{
  "email": "valid@example.com",
  "password": "password123",
  "passwordCheck": "password123",
  "nickname": "averylongnickname123"
}
```
#### ✅ **실패 응답 (400 Bad Request)**
```json
{
  "message": "닉네임은 2자 이상 12자 이하이어야 합니다."
}
```

#### 📌 **회원가입 요청 (POST /api/auth/signup - 실패 케이스, 닉네임 특수문자 포함)**
```json
{
  "email": "valid@example.com",
  "password": "password123",
  "passwordCheck": "password123",
  "nickname": "invalid@name"
}
```
#### ✅ **실패 응답 (400 Bad Request)**
```json
{
  "message": "닉네임은 특수문자 없이 영문, 숫자, 한글만 사용할 수 있습니다."
}
```

---

## 비밀번호 인코딩

### 1️⃣ 비밀번호 인코딩이 필요한 이유
- 비밀번호를 **평문(Plain Text)** 으로 저장하면 **보안상 매우 취약**함.
- 데이터베이스가 유출되었을 경우, **사용자의 비밀번호가 그대로 노출되는 위험**이 있음.
- `PasswordEncoder`를 사용하여 비밀번호를 **암호화(해싱)** 하여 저장하면 보안을 강화할 수 있음.

### 2️⃣ 📌 `PasswordEncoder` 설정 (BCrypt 사용)
Spring Security에서 제공하는 `BCryptPasswordEncoder`를 사용하여 비밀번호를 안전하게 암호화할 수 있음.

#### 🔹 `build.gradle`
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security'
}
```
```java
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

✅ **설명:**
- `BCryptPasswordEncoder`는 **솔트(Salt)** 를 자동으로 추가하여, **같은 비밀번호라도 매번 다른 해시값을 생성**함.
- 해싱된 비밀번호는 복호화가 불가능하며, 로그인 시 `matches()` 메서드로 비교함.


### 3️⃣ 📌 회원가입 시 비밀번호 암호화 적용
비밀번호를 평문으로 저장하지 않고, `BCryptPasswordEncoder`를 사용하여 암호화한 후 저장해야 함.

#### 📌 `AuthService`에 비밀번호 암호화 적용
```java
@Service
public class AuthService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerAccount(@Valid SignupRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        if (profileRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
        
        /*
         *  추가된 부분 
         */ 
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        
        Account account = Account.builder()
                .email(request.getEmail())
                .password(encodedPassword) // 암호화된 비밀번호 저장
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
}
```

---

## Backend 로그인

### 1️⃣ 로그인 기능 개요
- 사용자가 이메일과 비밀번호를 입력하여 인증을 수행하는 기능
- 입력된 이메일을 기준으로 사용자 정보를 조회한 후, 저장된 비밀번호와 비교하여 인증 수행
- 인증 성공 시 사용자 정보를 반환하여 클라이언트가 활용할 수 있도록 함
- 성공적으로 인증되면 **세션 방식** 또는 **JWT 토큰 발급 방식** 중 하나를 사용하여 로그인 상태 유지

### 2️⃣ 📌 로그인 요청 DTO (`LoginRequest`)
로그인 요청 시 클라이언트가 전송하는 데이터 구조를 정의합니다.
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
```

✅ **설명:**
- `email`: 로그인할 이메일 (필수 입력, 올바른 형식 검사)
- `password`: 로그인할 비밀번호 (필수 입력)

### 3️⃣ 📌 로그인 응답 DTO (`UserResponse`)
로그인 성공 시 반환할 사용자 정보 DTO입니다.
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String email;
    private String nickname;
    private String imageUrl;
    private String statusMessage;
    private UserRole role;
}
```

✅ **설명:**
- `email`: 사용자 이메일
- `nickname`: 사용자 닉네임
- `imageUrl`: 프로필 이미지 URL
- `statusMessage`: 상태 메시지
- `role`: 사용자 역할

### 4️⃣ 📌 로그인 서비스 (`AuthService`)
이메일을 기반으로 사용자를 조회하고, 비밀번호를 비교하여 인증한 후 사용자 정보를 반환합니다.
```java
@Service
public class AuthService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 로그인 처리
     *
     * @param request 로그인 요청 DTO
     * @throws IllegalArgumentException 이메일 없을 시 또는 비밀번호 틀렸을 시 예외 발생
     */
    public UserResponse login(@Valid LoginRequest request) {
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
        
        // 프로필 정보 조회
        Profile profile = profileRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("프로필 정보를 찾을 수 없습니다."));
        
        // 사용자 정보 반환
        return UserResponse.builder()
                .email(account.getEmail())
                .nickname(profile.getNickname())
                .imageUrl(profile.getImageUrl())
                .statusMessage(profile.getStatusMessage())
                .role(account.getRole())
                .build();
    }
}
```

✅ **설명:**
- `findByEmail(request.getEmail())` → 이메일을 기준으로 사용자 조회
- `passwordEncoder.matches(request.getPassword(), account.getPassword())` → 비밀번호 검증
- `findByAccount(account)` → 프로필 정보를 조회하여 함께 반환
- `UserResponse` 객체를 생성하여 사용자 정보를 반환

### 5️⃣ 📌 로그인 컨트롤러 (`AuthApiController`)
로그인 API 엔드포인트를 제공합니다.

```java
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {
    private final AuthService authService;

    /**
     * 로그인 API
     *
     * @param request 로그인 요청 데이터 (DTO)
     * @return 사용자 정보 또는 에러 응답
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().getFirst().getDefaultMessage());
        }

        try {
            UserResponse userResponse = authService.login(request);
            return ResponseEntity.ok(userResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
```

✅ **설명:**
- 로그인 요청을 받아 **이메일과 비밀번호 검증**
- 인증 성공 시 **사용자 정보를 포함한 `UserResponse` 반환**
- 인증 실패 시 `400 Bad Request` 응답 반환

### 6️⃣ 📌 로그인 API 테스트
#### **📌 로그인 요청 (POST /api/auth/login) - 성공 케이스**
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
#### ✅ **성공 응답 (200 OK)**
```json
{
  "email": "user@example.com",
  "nickname": "testuser",
  "imageUrl": "https://example.com/image.jpg",
  "statusMessage": "Hello, world!",
  "role": "USER"
}
```

#### **📌 로그인 실패 (이메일 또는 비밀번호 오류)**
```json
{
  "email": "wrong@example.com",
  "password": "wrongpassword"
}
```
#### ✅ **실패 응답 (400 Bad Request)**
```json
{
  "message": "이메일 또는 비밀번호가 잘못되었습니다."
}
```

---

## JWT 기반 로그인 설계

### 1️⃣ JWT란 무엇인가?
- **JWT (JSON Web Token) 개요**
JWT (JSON Web Token)는 **사용자 인증 및 정보 교환을 위한 토큰 기반 인증 방식**입니다. JWT는 **디지털 서명이 포함된 토큰**으로, 클라이언트와 서버 간의 보안성을 유지하면서 인증 정보를 주고받을 수 있습니다.

- ✅ **JWT의 주요 특징**
    - **무상태 (Stateless) 인증** → 서버가 세션을 유지할 필요 없이 클라이언트가 JWT를 저장하고 사용
    - **JSON 기반** → 가볍고 효율적인 데이터 전송 가능
    - **서명(Signature) 포함** → 데이터가 변조되지 않음을 보장
    - **토큰 만료 기한 (Expiration) 설정 가능** → 보안 강화

- ✅ **JWT의 구조**
JWT는 **세 부분(Header, Payload, Signature)** 으로 구성되며, 각각 `.`으로 구분됩니다.
```
헤더(Header).페이로드(Payload).서명(Signature)
```

- **JWT 예시**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwibmlja25hbWUiOiJ0ZXN0dXNlciIsInJvbGUiOiJVU0VSIiwiZXhwIjoxNzAwMDAwMDAwfQ.2D_BXGmRRx8vUs_tZyI4f8i1AxXWGytrQ
```
- ✅ **설명**
    - **Header (헤더)** → 토큰의 타입과 암호화 알고리즘 정보를 포함
    - **Payload (페이로드)** → 사용자 정보(이메일, 권한 등)와 토큰 만료 시간을 포함
    - **Signature (서명)** → 토큰이 변조되지 않았음을 검증하는 값
📌 **JWT는 서버에서 발급하고, 클라이언트(브라우저, 앱 등)에서 저장한 후 API 요청 시 사용합니다.**

### 2️⃣ 📌 JWT 사용을 위한 의존성 추가
JWT를 사용하려면 **`build.gradle`** 에 **`jjwt`** 라이브러리를 추가해야 합니다.
```gradle
dependencies {
    implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
    implementation 'io.jsonwebtoken:jjwt-impl:0.11.5'
    implementation 'io.jsonwebtoken:jjwt-jackson:0.11.5' // JSON 처리용
}
```

✅ **설명**
- `jjwt`는 **JWT 토큰을 생성, 검증, 파싱하는 기능을 제공하는 라이브러리**입니다.


### 3️⃣ 📌 JWT를 이용한 로그인 구현

#### 🔹 **JWT 생성 (`JwtUtil` 클래스)**

JWT를 생성하고 검증하는 유틸리티 클래스를 만듭니다.

```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = System.getenv("JWT_SECRET_KEY"); // .env 파일에서 가져오기
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24시간

    public String generateToken(UserResponse userResponse) {
        return Jwts.builder()
                .setSubject(userResponse.getEmail()) // 이메일을 subject로 설정
                .claim("nickname", userResponse.getNickname())
                .claim("imageUrl", userResponse.getImageUrl())
                .claim("statusMessage", userResponse.getStatusMessage())
                .claim("role", userResponse.getRole().toString())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간 설정
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS512) // 서명 알고리즘 설정
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
✅ **설명**
- `.env` 파일에 `JWT_SECRET_KEY=your-secure-random-key` 값을 추가하고, `System.getenv("JWT_SECRET_KEY")`로 환경 변수에서 가져옴.
- `generateToken(userResponse)` → JWT 토큰을 생성하고 사용자 정보를 포함하여 반환
- `extractClaims(token)` → 토큰에서 사용자 정보를 추출
- `extractEmail(token)` → 토큰에서 이메일을 추출
- `validateToken(token)` → 토큰의 유효성을 검증

#### 🔹 **로그인 시 JWT 발급 (`AuthService`)**

로그인 성공 시, JWT를 생성하고 반환합니다.

```java
@Service
public class AuthService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 로그인 처리
     *
     * @param request 로그인 요청 DTO
     * @throws IllegalArgumentException 이메일 없을 시 또는 비밀번호 틀렸을 시 예외 발생
     */
    public String login(@Valid LoginRequest request) {
        // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

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

        return jwtUtil.generateToken(userResponse);
    }
}
```
✅ **설명**
- 로그인 성공 시 `jwtUtil.generateToken(userResponse)`를 호출하여 JWT 생성
- JWT는 이후 클라이언트에서 저장하여 API 요청 시 사용

#### 🔹 **로그인 API (`AuthApiController`)**
```java
/**
 * 로그인 API
 *
 * @param request 로그인 요청 데이터 (DTO)
 * @return JWT 토큰 또는 에러 응답
 */
@PostMapping("/login")
public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
    if (result.hasErrors()) {
        return ResponseEntity.badRequest().body(result.getAllErrors().getFirst().getDefaultMessage());
    }

    try {
        String token = authService.login(request);  // ✅ 로그인 후 JWT 반환받기
        return ResponseEntity.status(HttpStatus.OK).body(token);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
```

✅ **설명**
- `authService.login(request)`를 호출하여 **로그인 검증 후 JWT 반환**
- `ResponseEntity.status(HttpStatus.OK).body(token)`을 사용하여 **토큰을 직접 응답으로 반환**
- 에러 발생 시 `400 Bad Request`와 함께 메시지를 반환


---

## Refresh Token 설계

### 📌 Access Token & Refresh Token 기반 인증 시스템 설계

### 1️⃣ **Access Token과 Refresh Token이란?**
#### ✅ **Access Token**
- 사용자가 로그인하면 발급되는 **JWT(JSON Web Token)**.
- API 요청 시 `Authorization: Bearer {AccessToken}` 헤더에 포함하여 **사용자 인증을 수행**.
- 보안 강화를 위해 **만료 시간이 짧음** (예: 30분~1시간).

#### ✅ **Refresh Token**
- Access Token이 만료되었을 때 **새로운 Access Token을 발급받기 위한 토큰**.
- 클라이언트는 만료된 Access Token을 갱신할 때 사용.
- **서버에서 DB에 저장하여 관리** (보안 강화 목적).
- 보통 **7일 ~ 30일** 정도의 긴 만료 시간을 가짐.

✅ **Access Token & Refresh Token 흐름**
1. 사용자가 로그인하면 **Access Token & Refresh Token을 함께 발급**.
2. 클라이언트는 Access Token을 사용하여 API 요청을 수행.
3. Access Token이 만료되면 **Refresh Token을 이용해 새로운 Access Token을 발급**.
4. Refresh Token도 만료되면 **다시 로그인해야 함**.

### 2️⃣ **Refresh Token 구현 (DB 저장 필요)**

#### 🔹 **RefreshToken 엔티티 생성**
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account; // 사용자 계정

    @Column(nullable = false, unique = true)
    private String token; // Refresh Token 값

    @Column(nullable = false)
    private LocalDateTime expirationDate; // 만료 시간
}
```

#### 🔹 **RefreshToken 저장 및 조회하는 Repository**
```java
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByAccount(Account account);
}
```

#### 🔹 **Access & Refresh Token을 함께 발급하는 서비스 구현 (`AuthService`)**
```java
public Map<String, String> login(@Valid LoginRequest request) {
    // 이메일로 사용자 조회
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

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

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .token(refreshToken)
                        .account(account)
                        .expirationDate(LocalDateTime.now().plusDays(7))
                        .build()
        );

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
}
```
#### 🔹 **로그인 API (Access & Refresh Token 반환) (`AuthApiController`)**
```java
@PostMapping("/login")
public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
    if (result.hasErrors()) {
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", result.getAllErrors().getFirst().getDefaultMessage()));
    }
    
    try {
        Map<String, String> tokens = authService.login(request); // ✅ Access & Refresh Token 반환
        return ResponseEntity.ok(tokens); // ✅ JSON 형식으로 응답
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
    }
}
```

### 3️⃣ **JWT 생성 및 검증 (`JwtUtil`)**
```java
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
```

✅ ** 🔹 Access Token 생성 설명 **
| 메서드 | 설명 |
|--------|------|
| `Jwts.builder()` | JWT를 생성하는 빌더 객체 생성 |
| `.setSubject(userResponse.getEmail())` | **JWT의 고유한 subject 설정** (이메일을 사용하여 식별) |
| `.claim("nickname", userResponse.getNickname())` | 사용자 닉네임을 추가 (Custom Claims) |
| `.claim("imageUrl", userResponse.getImageUrl())` | 프로필 이미지 URL 추가 |
| `.claim("statusMessage", userResponse.getStatusMessage())` | 사용자 상태 메시지 추가 |
| `.claim("role", userResponse.getRole().toString())` | 사용자의 역할(Role) 추가 |
| `.setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))` | **토큰 만료 시간 설정** (현재 시간 + 지정된 만료 시간) |
| `.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)` | **HMAC-SHA256 알고리즘으로 서명하여 보안 유지** |
| `.compact()` | **JWT 문자열로 변환하여 반환** |

```java
    public String generateRefreshToken(UserResponse userResponse) {
        return Jwts.builder()
                .setSubject(userResponse.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }
```

✅ ** 🔹 Refresh Token 생성 설명 **
| 메서드 | 설명 |
|--------|------|
| `Jwts.builder()` | JWT를 생성하는 빌더 객체 생성 |
| `.setSubject(userResponse.getEmail())` | **Refresh Token에도 Subject(email)를 설정** (Access Token과 동일) |
| `.setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))` | **Refresh Token의 만료 시간 설정** (Access Token보다 길게 설정) |
| `.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)` | **HMAC-SHA256 알고리즘으로 서명** (보안 유지) |
| `.compact()` | **JWT 문자열로 변환하여 반환** |

---

## Refresh Token을 이용한 Access Token 갱신

### 1️⃣ **Refresh Token을 이용한 Access Token 갱신 API (`AuthApiController`)**
```java
/**
 * Refresh Token을 이용한 Access Token 갱신 API
 *
 * @param request 클라이언트에서 전송한 Refresh Token (JSON 형식)
 * @return 성공 시 새로운 Access Token 반환 또는 에러 응답
 */
@PostMapping("/refresh")
public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
    try {
        String newAccessToken = authService.refreshAccessToken(request.get("refreshToken"));
        return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
```

### 2️⃣ **Refresh Token을 검증하고 새로운 Access Token을 발급하는 서비스 (`AuthService`)**
```java
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
```

✅ **설명**
- Refresh Token이 유효한지 검증 후, 새로운 Access Token을 생성하여 반환.
- Refresh Token이 만료되었으면 **새로운 Access Token 발급 불가능** → 다시 로그인 필요.
- 검증된 사용자의 정보를 기반으로 **새로운 JWT 발급**.

---

## 프로필 수정 API 구현

### 📌 Spring Security & @AuthenticationPrincipal 기반

### 1️⃣ 프로필 수정 요청 처리 (`ProfileApiController`)

```java
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
            return ResponseEntity.ok(token);   // ✅ JSON 형식으로 응답
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
```

#### ✅ `@AuthenticationPrincipal`이란?
`@AuthenticationPrincipal`은 Spring Security에서 제공하는 어노테이션으로, 현재 인증된 사용자의 정보를 컨트롤러에서 바로 주입받을 수 있도록 해준다. JWT 기반 인증을 사용하는 경우, SecurityContext에 저장된 `UserPrincipal` 객체를 자동으로 매핑해준다.

**📌 장점**
- 컨트롤러에서 별도로 JWT를 해석하지 않아도 됨
- `SecurityContextHolder`를 직접 조회할 필요 없이 간결한 코드 유지 가능
- 인증된 사용자 정보(`UserPrincipal`)를 바로 활용 가능

**📌 예제**
```java
@PatchMapping
public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileEditRequest request,
                                       BindingResult result,
                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {
    if (userPrincipal == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 사용자입니다.");
    }
    Long userId = userPrincipal.getId();
    // ... 프로필 수정 로직
}
```

#### ✅ `UserPrincipal`이란?
`UserPrincipal`은 Spring Security에서 사용자 정보를 관리하는 객체로, `UserDetails` 인터페이스를 구현하여 사용자의 이메일, 닉네임, 프로필 이미지 등의 정보를 포함할 수 있다. JWT 기반 인증에서는 사용자의 ID 대신 이메일을 활용할 수 있도록 설계한다.
`@AuthenticationPrincipal`을 통해 인증된 사용자의 정보를 컨트롤러에서 접근할 수 있도록 설계되었다.

**📌 역할**
- 현재 인증된 사용자의 정보를 포함 (이메일, 닉네임, 프로필 이미지 등)
- Spring Security의 `UserDetails` 인터페이스를 구현하여 보안 설정과 연동 가능
- `@AuthenticationPrincipal`을 통해 컨트롤러에서 직접 접근 가능

```java
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
```

#### ✅ `UserPrincipal`에서 사용하기 위한 `UserRole` 수정

```java
@Getter
@AllArgsConstructor
public enum UserRole {
    USER("ROLE_USER"),  // 일반 사용자
    MANAGER("ROLE_MANAGER"), // 제한적인 관리자 권한
    ADMIN("ROLE_ADMIN"), // 관리자
    SUPER_ADMIN("ROLE_SUPER_ADMIN"); // 최상위 관리자 (모든 권한 보유)

    private final String roleName;
}
```

---

### 2️⃣ 요청 데이터 DTO (`ProfileEditRequest`)

```java
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileEditRequest {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 12, message = "닉네임은 2자 이상 12자 이하이어야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "닉네임은 특수문자 없이 영문, 숫자, 한글만 사용할 수 있습니다.")
    private String nickname;

    private String imageUrl;
}
```

---

### 3️⃣ Spring Security에서 `@AuthenticationPrincipal`을 사용하기 위한 설정

#### ✅ JWT 인증 필터 (`JwtAuthenticationFilter`)
```java
@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null && jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractEmail(token);
            Claims claims = jwtUtil.extractClaims(token);

            UserResponse userResponse = new UserResponse(
                    email,
                    claims.get("nickname", String.class),
                    claims.get("imageUrl", String.class),
                    claims.get("statusMessage", String.class),
                    UserRole.valueOf(claims.get("role", String.class))
            );

            UserPrincipal userPrincipal = new UserPrincipal(
                    userResponse.getEmail(),
                    userResponse.getNickname(),
                    userResponse.getImageUrl(),
                    userResponse.getStatusMessage(),
                    userResponse.getRole()
            );

            Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, token, userPrincipal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

#### ✅ Spring Security 설정 (`SecurityConfig`) - 추가해야 할 부분 (JWT 적용)

**📌 JWT 인증을 사용하려면 아래 설정이 추가되어야 함**  
✔ JwtAuthenticationFilter 적용 → 요청이 들어올 때 JWT를 검증해야 함  
✔ Session을 사용하지 않도록 설정 → STATELESS 모드 적용  
✔ UsernamePasswordAuthenticationFilter 이전에 JWT 필터 추가  

```java
@AllArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // ✅ JWT 기반 인증에서는 CSRF 필요 없음
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ✅ JWT는 세션 미사용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/h2-console/**").permitAll() // ✅ 로그인 관련 API는 인증 없이 접근 가능
                        .anyRequest().authenticated() // ✅ 그 외 모든 요청은 인증 필요
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // ✅ JWT 필터 적용
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)); // ✅ H2 Console을 위한 설정 유지

        return http.build();
    }
}
```

---

### 4️⃣ 프로필 업데이트 서비스 (`ProfileService`)

```java
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

        // 닉네임 중복 체크
        if (profileRepository.existsByNickname(request.getNickname()))
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");

        // 프로필 정보 조회
        Profile profile = profileRepository.findByAccount(account)
                .orElseThrow(() -> new IllegalArgumentException("프로필 정보를 찾을 수 없습니다."));

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
```

---

## 비밀번호 변경 API 구현

### **1️⃣ 비밀번호 변경 요청 DTO (`PasswordChangeRequest`)**
```java
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String passwordNow;  // ✅ 현재 비밀번호

    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
    private String passwordNew;  // ✅ 새 비밀번호

    @NotBlank(message = "새 비밀번호 확인을 입력해주세요.")
    private String passwordCheck;  // ✅ 새 비밀번호 확인

    @AssertTrue(message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
    public boolean isPasswordMatch() {
        return passwordNew != null && passwordNew.equals(passwordCheck);
    }

    @AssertTrue(message = "새 비밀번호는 기존 비밀번호와 달라야 합니다.")
    public boolean isNewPasswordDifferent() {
        return passwordNew != null && passwordNow != null && !passwordNew.equals(passwordNow);
    }
}
```
✔ **현재 비밀번호, 새 비밀번호, 새 비밀번호 확인 필드 포함**  
✔ **비밀번호 최소/최대 길이 제한 (`8~20자`)**  
✔ **비밀번호 일치 여부를 검증하는 `isPasswordMatch()` 추가**  
✔ **기존 비밀번호와 새 비밀번호의 일치 여부를 검증하는 `isNewPasswordDifferent()` 추가**  

---

### **2️⃣ 비밀번호 변경 컨트롤러 (`AuthApiController`)**
```java
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private final AuthService authService;

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
}
```
✔ **현재 로그인한 사용자의 정보를 `@AuthenticationPrincipal`로 가져옴**  
✔ **`AuthService`를 호출하여 비밀번호 변경 수행**  
✔ **비밀번호 변경 성공 시 `"비밀번호 변경이 완료되었습니다."` 반환**  
✔ **오류 발생 시 적절한 메시지를 반환**  

---

### **3️⃣ 비밀번호 변경 서비스 (`AuthService`)**
```java
@Service
public class AuthService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}
```
✔ **사용자 이메일을 기반으로 계정(Account) 조회**  
✔ **저장된 비밀번호와 입력된 비밀번호 검증** 
✔ **새로운 비밀번호를 암호화한 후 저장**  

---

### **📌 응답 (백엔드 → 프론트엔드)**
| HTTP 응답 코드 | 설명 |
|---|---|
| `200 OK` | 비밀번호 변경 성공 |
| `400 Bad Request` | 현재 비밀번호 불일치, 새 비밀번호 불일치 등 유효성 검사 실패 |
| `401 Unauthorized` | 인증되지 않은 요청 (JWT 누락 등) |
| `500 Internal Server Error` | 서버 내부 오류 발생 |

---

## **🚀 기능 요약**
✅ **사용자가 현재 비밀번호를 입력하고, 새로운 비밀번호로 변경 가능**  
✅ **비밀번호 변경 시 유효성 검사 (`빈 값`, `최소 8자`, `최대 20자`, `일치 여부`) 적용**  
✅ **비밀번호 변경 후 `PasswordEncoder`를 사용해 암호화하여 저장**  
✅ **새 비밀번호가 기존 비밀번호와 같을 경우 예외 처리**  
✅ **Spring Security의 `@AuthenticationPrincipal`을 사용하여 현재 사용자 정보 가져오기**  

--

## Postgre SQL 연동

다음은 Spring Boot 프로젝트에서 PostgreSQL을 연동하기 위한 설정입니다.

```properties
# 🗄️ PostgreSQL 데이터베이스 설정
spring.datasource.url=jdbc:postgresql://localhost:5432/sometodo
spring.datasource.username=postgres
spring.datasource.password=p

# ⚙️ Hibernate 설정
spring.jpa.hibernate.ddl-auto=create
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect 

# 📂 초기 데이터 로드 설정
spring.datasource.data=classpath:/import.sql 
spring.datasource.initialization-mode=always 
```

---

### 📝 **설정 설명**

- 1️⃣ **`spring.datasource.url`** → PostgreSQL 데이터베이스의 연결 URL을 설정합니다. (`localhost:5432/sometodo` 데이터베이스 사용)
- 2️⃣ **`spring.datasource.username`** / **`spring.datasource.password`** → PostgreSQL에 연결하기 위한 사용자 이름과 비밀번호를 설정합니다.
- 3️⃣ **`spring.jpa.hibernate.ddl-auto=create`** → 애플리케이션 실행 시 기존 테이블을 삭제하고 새롭게 생성합니다.
   - 🔹 **`update`** : 기존 테이블을 유지하면서 변경된 스키마를 적용 (✅ 운영 환경 추천)
   - 🔹 **`none`** : Hibernate의 자동 스키마 관리를 비활성화 (✅ 운영 환경에서 추천 가능)
- 4️⃣ **`spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect`** → PostgreSQL에 최적화된 Hibernate의 SQL 생성 전략을 설정합니다.
- 5️⃣ **`spring.datasource.data=classpath:/import.sql`** → `import.sql` 파일을 이용해 애플리케이션 실행 시 초기 데이터를 삽입합니다.
- 6️⃣ **`spring.datasource.initialization-mode=always`** → 애플리케이션이 실행될 때마다 `import.sql`을 실행하여 초기 데이터를 로드합니다.

---

### ⚠️ **추가 고려 사항**
✅ **운영 환경에서는 `ddl-auto=update` 또는 `none`을 사용하는 것이 안전합니다.**  
✅ **`import.sql`을 이용한 데이터 삽입은 개발 및 테스트 환경에서만 사용하는 것이 좋습니다.**  
✅ **PostgreSQL과 연결 시 추가적인 네트워크 설정이 필요할 수도 있습니다.**  

---

### 🔗 **의존성 추가 (`build.gradle`)**
```gradle
dependencies {
    runtimeOnly 'org.postgresql:postgresql'
}
```

---

## 구글 로그인 연동

### ✅ 백엔드(Spring Boot)에서 Google 로그인 처리하는 방법 (google-api-client 없이)

이 문서는 안드로이드 앱에서 구글 로그인 후 전달된 `idToken`을 백엔드(Spring Boot)에서 **직접 검증하고** 유저 정보를 처리하는 방법을 정리한 개발 가이드입니다.

---

### 🔁 전체 흐름 요약

1. 안드로이드 앱에서 Google Sign-In SDK로 로그인 수행
2. 앱에서 받은 `idToken` 을 백엔드로 전송
3. 백엔드는 전달받은 `idToken`을 **공개키 기반으로 검증**하고, 유저 정보를 처리 (회원가입/로그인)
4. 필요시 JWT 토큰 발급 등 후속 처리

> `idToken` 으로 검증을 하면, email, name, picture 데이터를 얻을 수 있다.

---

### ❓ 왜 `google-api-client`를 사용하지 않는가?

`google-api-client`는 구글에서 공식 제공하는 클라이언트 라이브러리지만, 다음과 같은 단점이 있습니다:

- ❌ **라이브러리 크기가 크고 무겁다**: 종속성 계층이 복잡함
- ⚠️ **보안 취약점(CVE)** 경고가 자주 발생 (예: JWT 서명 검증 관련 이슈)
- 🧩 **의존성 충돌 위험**: 다른 라이브러리와의 버전 충돌 가능성 존재

✅ 대신 `nimbus-jose-jwt`를 사용하면:
- 경량 라이브러리로 필요한 기능만 사용 가능
- JWT 검증 기능에 특화되어 있음
- 구글 공개 키를 활용한 직접 검증이 가능
- 보안성, 유지보수성, 유연성 모두 뛰어남

---

### 📦 백엔드에서 처리할 내용

#### 1️⃣ 의존성 추가 (`build.gradle`)

```gradle
dependencies {
    implementation 'com.nimbusds:nimbus-jose-jwt:9.37.3' // JWT 파싱 및 검증용
}
```

> `nimbus-jose-jwt`는 구글 ID 토큰(JWT) 검증에 자주 쓰이는 경량 라이브러리입니다.

---

#### 2️⃣ GoogleLoginRequest DTO

```java
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleLoginRequest {
    @NotBlank(message = "ID 토큰이 필요합니다.")
    private String idToken; // ✅ 서버에서 구글 인증 검증용으로 반드시 필요
}
```

---

#### 3️⃣ GoogleTokenVerifier (직접 검증)

```java
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class GoogleTokenVerifier {

    private static final String GOOGLE_JWK_URL = "https://www.googleapis.com/oauth2/v3/certs";

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    public JWTClaimsSet verify(String idToken) throws Exception {
        // try-with-resources 구문으로 HttpClient와 HttpResponse 자동 관리
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GOOGLE_JWK_URL))
                    .build();

            // HttpResponse<String>를 try-with-resources로 사용하여 자동으로 자원 관리
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 받은 JSON을 JWKSet으로 파싱
            JWKSet jwkSet = JWKSet.parse(response.body());

            // JWKSet에서 키를 가져와서 검증 수행
            JWKSource<SecurityContext> keySource = new ImmutableJWKSet<>(jwkSet);

            // JWT 처리기 설정
            DefaultJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();

            // JWSVerificationKeySelector를 사용하여 서명 검증
            JWSVerificationKeySelector<SecurityContext> keySelector =
                    new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, keySource);
            jwtProcessor.setJWSKeySelector(keySelector);

            JWTClaimsSet claimsSet = jwtProcessor.process(idToken, null);

            // audience 검사
            if (!claimsSet.getAudience().contains(clientId)) {
                throw new IllegalArgumentException("Invalid audience");
            }

            // 만료 시간 검사
            if (claimsSet.getExpirationTime().getTime() < System.currentTimeMillis()) {
                throw new IllegalArgumentException("Token expired");
            }

            return claimsSet;
        }
    }
}
```

---

#### 4️⃣ Controller에서 처리

```java
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    AuthService authService;
    @Autowired
    GoogleTokenVerifier tokenVerifier;

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
}
```

---

#### 5️⃣ Service 에서 처리

```java
@Service
public class AuthService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtUtil jwtUtil;
    
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
}
```

### Ⅰ. 고유 닉네임 생성기

#### **기능 설명**:
- **닉네임 중복 검사**: `profileRepository.existsByNickname`을 사용하여 주어진 **닉네임**이 이미 존재하는지 확인합니다.
- **고유 닉네임 생성**: 중복된 닉네임이 있다면, `nickname + "s"` 뒤에 랜덤 숫자를 붙여서 **새로운 닉네임**을 찾습니다.
- **랜덤 숫자 생성 범위**: **10만**부터 **99만 9999**까지의 숫자를 사용합니다.

#### **사용**:
```java
private String generateUniqueNickname(String nickname) {
    String uniqueNickname = nickname;
    Random random = new Random();

    // 중복된 닉네임이 있는지 확인
    while (profileRepository.existsByNickname(uniqueNickname)) {
        uniqueNickname = nickname + "s" + (100000 + random.nextInt(900000)); // 10만 부터 99만9999 까지 랜덤 숫자
    }

    return uniqueNickname;
}
```

#### **설명**:
- 이 메서드는 중복된 닉네임을 처리하고, 중복되지 않는 고유한 닉네임을 보장합니다.
- **실시간으로 닉네임의 중복 여부를 검사**하여 유저가 등록할 수 있는 고유 닉네임을 제공합니다.

---

### Ⅱ. 계정 정보로 토큰 생성

이 메서드는 **계정 정보(Account)**와 **프로필 정보(Profile)**를 사용하여 **JWT 토큰**을 생성합니다.  
계정에 해당하는 **프로필**이 없으면 예외를 발생시키고, 프로필이 존재하면 **Access Token**과 **Refresh Token**을 생성하여 반환합니다.  

이 코드는 기존 로그인에 있었던 코드를 가져와서 하나의 메소드로 만들어서 공용으로 사용할 수 있도록 하였습니다.

#### **기능 설명**:
- **프로필 정보 조회**: `profileRepository.findByAccount(account)`를 통해 **프로필** 정보를 조회합니다. 프로필이 존재하지 않으면 `IllegalArgumentException` 예외를 던집니다.
- **토큰 생성**: 사용자 정보 (`email`, `nickname`, `imageUrl`, `statusMessage`, `role`)를 포함하여 **Access Token**과 **Refresh Token**을 생성합니다.
- **Refresh Token 관리**: 이미 **Refresh Token**이 존재하면 갱신하고, 그렇지 않으면 새로 생성하여 저장합니다.

#### **사용**:
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

    Map<String, String> tokens = new HashMap<>();
    tokens.put("accessToken", accessToken);
    tokens.put("refreshToken", refreshToken);

    return tokens;
}
```

#### **설명**:
- **프로필 정보**가 없다면 예외를 던지고, **프로필 정보**가 존재하면 **사용자 정보를 포함한 토큰**을 생성합니다.
- **`JWT 토큰`**은 **사용자 정보**(`email`, `nickname`, `imageUrl` 등)와 함께 **Access Token**과 **Refresh Token**을 생성합니다.
- **Refresh Token**은 기존의 토큰이 있으면 **갱신**하고, 없으면 **새로 저장**합니다.

#### **주의점**:
- 이 메서드는 **프로필 정보가 반드시 있어야**만 정상적으로 작동하므로, 프로필 정보가 없을 경우 예외를 던져 처리합니다.
- **Refresh Token**은 **7일** 후 만료되도록 설정되어 있으며, 갱신되는 주기에 따라 **새로운 토큰**을 저장합니다.

---




