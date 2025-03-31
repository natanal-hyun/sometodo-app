# Some To Do (일정 및 가계부 관리 앱)

## Frontend 개발 노트

## 📜 목차

### 프론트엔드 (Android)
- [Splash Screen](#splash-screen)
- [XML Hard Coding](#xml-hard-coding)
- [로그인 화면 구성](#로그인-화면-구성)
- [회원가입 화면 구성](#회원가입-화면-구성)
- [Retrofit 설정](#retrofit-설정)
- [DTO 설정](#dto-설정)
- [회원가입 기능 구현 및 테스트](#회원가입-기능-구현-및-테스트)
- [로그인 기능 구현](#로그인-기능-구현)
- [JWT 토큰 해석 및 캐싱 방식](#jwt-토큰-해석-및-캐싱-방식)
- [JWT 기반 API 요청 및 자동 갱신 구현](#jwt-기반-api-요청-및-자동-갱신-구현)
- [하단 바 Navigation](#하단-바-navigation)
- [Settings Activity 구성](#settings-activity-구성)
- [Settings 화면 구성](#settings-화면-구성)
- [Settings - 프로필 수정 페이지](#settings---프로필-수정-페이지)
- [프로필 수정 기능 구현](#프로필-수정-기능-구현)
- [Settings - 비밀번호 변경](#settings---비밀번호-변경)
- [구글 로그인 연동](#구글-로그인-연동)

---

## Splash Screen
- **📌 The application should not provide its own launch screen 오류 해결 방법**

### 1. ⚠️ 경고 메시지
> The application should not provide its own launch screen More... (Ctrl+F1)
> Inspection info: Starting in Android 12 (API 31+), the application's Launch Screen is provided by the system and the application should not create its own, otherwise the user will see two splashscreens.
> Please check the SplashScreen class to check how the Splash Screen can be controlled and customized.
> Issue id: CustomSplashScreen

### 2. 🔍 경고 원인
- **Android 12 (API 31) 이상에서는 시스템에서 기본적으로 Splash Screen을 제공**  
- 기존의 `SplashActivity` 방식으로 추가하면 **이중으로 스플래시 화면이 나타남**  
- 따라서 **개발자가 직접 Splash Screen을 만들 필요 없음**

### 3. ✅ 해결 방법  
- **🔹 방법 1: `androidx.core:core-splashscreen` 라이브러리 사용 (권장)**
> Android의 공식 `SplashScreen API`를 활용하여 **모든 API 버전에서 일관된 스플래시 화면을 적용**할 수 있음.

    - <strong> 1️⃣ gradle/libs.versions.toml 파일 수정 </strong>
    ```toml
    [versions]
    splashscreen = "1.0.1"

    [libraries]
    splashscreen = { module = "androidx.core:core-splashscreen", version.ref = "splashscreen" }
    ```
    
    - <strong> 2️⃣ build.gradle.kts 에 의존성 추가 </strong>
    ```gradle
    dependencies {
        implementation(libs.splashscreen)
    }
    ```
    
    - <strong> 3️⃣ themes.xml 에 스플래시 테마 추가 </strong>
    ```xml
    <!-- 스플래시 테마 -->
    <style name="Theme.ToDoList.Splash" parent="Theme.SplashScreen">
        <!-- 배경색 -->
        <item name="windowSplashScreenBackground">#6200EE</item>

        <!-- 앱 아이콘 -->
        <item name="windowSplashScreenAnimatedIcon">@mipmap/ic_launcher</item>

        <!-- 스플래시 이후 적용할 앱 기본 테마 -->
        <item name="postSplashScreenTheme">@style/Theme.ToDoList</item>
    </style>
    ```
    
    - <strong> 4️⃣ themes.xml 에 스플래시 테마 추가 </strong>
    ```xml
    <activity
        android:name=".MainActivity"
        android:exported="true"
        android:theme="@style/Theme.YourApp.Splash"> <!-- 스플래시 테마 적용 -->
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
    ```
    
    - <strong> 5️⃣ MainActivity에서 SplashScreen API 활용 </strong>
    ```java
    public class MainActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            SplashScreen.installSplashScreen(this); // 스플래시 적용
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        }
    }
    ```
    
---

## XML Hard Coding

### 1. ⚠️ 경고 메시지
> Hardcoded string "앱 로고", should use @string resource More... (Ctrl+F1) 
> Inspection info: Hardcoding text attributes directly in layout files is bad for several reasons:  * When creating configuration variations (for example for landscape or portrait) you have to repeat the actual text (and keep it up to date when making changes)  * The application cannot be translated to other languages by just adding new translations for existing string resources.  There are quickfixes to automatically extract this hardcoded string into a resource lookup.

### 2. 🔍 경고 원인
```go
Hardcoded string "앱 로고", should use @string resource
```
- 레이아웃 XML에서 문자열을 하드코딩하면 안 된다는 경고입니다. 
- 문자열을 @string 리소스로 저장한 후 참조하도록 수정해야 한다는 의미입니다.

```vbnet
Inspection info: Hardcoding text attributes directly in layout files is bad for several reasons:
```
- 하드코딩이 좋지 않은 이유:
    - 다른 화면 구성(예: 가로/세로 모드)에 맞춰 텍스트를 반복 작성해야 한다.
    - 앱을 다국어(다양한 언어)로 번역하려면 기존 문자열을 직접 수정해야 한다.
    - 유지보수 및 코드 가독성이 떨어진다.

### 3. ✅ 해결 방법
- <strong> 1️⃣ res/values/strings.xml 에 문자열 추가 </strong>
```xml
<resources>
    <string name="app_logo">앱 로고</string>
</resources>
```

- <strong> 2️⃣ XML에서 문자열 리소스 참조 </strong>
```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="앱 로고"/> <!-- ❌ 하드코딩 -->
```
```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/app_logo"/> <!-- ✅ 문자열 리소스 사용 -->
```

- <strong> 3️⃣ Login, Signup 에서 사용하기 위한 string 설정 </strong>
```xml
<resources>
    <!-- 앱 기본 정보 -->
    <string name="app_name">To Do List</string>
    <string name="app_logo">앱 로고</string>

    <!-- 인증(Auth) 관련 -->
    <string name="auth_email">이메일</string>
    <string name="auth_password">비밀번호</string>
    <string name="auth_password_check">비밀번호 확인</string>

    <string name="auth_login">이미 계정이 있습니다.</string>
    <string name="auth_sign_up">계정이 없습니다.</string>

    <!-- 버튼(Button) 관련 -->
    <string name="btn_login">로그인</string>
    <string name="btn_sign_up">회원가입</string>

    <string name="btn_login_for_google">구글로 로그인</string>

    <!-- 기타 -->
    <string name="or">OR</string>
</resources>
```

---

## 로그인 화면 구성
<img src="./img/auth_login.jpg" width="200" height="400" /> <br />
### 1. 📌 구성 요소
- 앱 로고
- 이메일 입력 필드
- 비밀번호 입력 필드
- 로그인 버튼
    - onClick = onLoginButtonClick 연결
- 회원가입 안내 (계정이 없습니다.)
    - 클릭 시 Signup Activity 실행
    ```java
    public void onSignupTextClick(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }
    ```
- 구분선 (OR)
- 구글 로그인 버튼
    - onClick = onLoginForGoogleButtonClick 연결



---

## 회원가입 화면 구성
<img src="./img/auth_signup.jpg" width="200" height="400" /> <br />
### 1. 📌 구성 요소
- 앱 로고
- 이메일 입력 필드
- 비밀번호 입력 필드
- 비밀번호 확인 입력 필드
- 회원가입 버튼
    - onClick = onSignupButtonClick 연결
- 로그인 안내 (이미 계정이 있습니다.)
    - 클릭 시 finish() 실행하여 Login Activity 로 돌아감
    ```java
    public void onLoginTextClick(View view) {
        finish();
    }
    ```
    


---

## Retrofit 설정

### 📌 안드로이드에서 Retrofit을 이용한 백엔드 API 연동 (Java 버전)

### 1️⃣ **Retrofit이란?**
- Retrofit은 안드로이드에서 **REST API와의 통신을 쉽게 도와주는 라이브러리**.
- JSON 데이터를 주고받을 때 Gson을 활용하여 자동 변환 가능.
- 백엔드(Spring Boot)와 HTTP 요청을 통해 데이터를 주고받기 위해 사용됨.

### 2️⃣ **Retrofit 설정 및 적용 순서**

#### 🔹 Step 1: `libs.versions.toml`에서 버전 관리 및 `build.gradle`에서 Retrofit 추가
Retrofit을 사용하려면 `libs.versions.toml`에 버전을 관리하고, `build.gradle`에서 필요한 라이브러리를 추가해야 함.

📌 **`libs.versions.toml`**
```toml
[versions]
retrofit = "2.9.0"
gson = "2.9.0"
okhttp = "4.9.1"
scalars = "2.9.0"

[libraries]
retrofit = { module = "com.squareup.retrofit2:retrofit", version.ref = "retrofit" }
gson = { module = "com.squareup.retrofit2:converter-gson", version.ref = "gson" }
okhttp = { module = "com.squareup.okhttp3:logging-interceptor", version.ref = "okhttp" }
scalars = { module = "com.squareup.retrofit2:converter-scalars", version.ref = "scalars" }
```

📌 **`build.gradle (Module: app)`**
```gradle
dependencies {
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.okhttp)
    implementation(libs.scalars)
}
```

✅ **설명**
- `libs.versions.toml`에서 버전을 정의하고, `build.gradle`에서 이를 참조하도록 구성.
- `retrofit` → REST API 호출을 쉽게 해줌.
- `gson` → JSON 데이터를 자동 변환.
- `okhttp` → 네트워크 요청을 로깅할 수 있도록 도와줌.
- `scalars` → JSON이 아닌 단순 문자열, 숫자, Boolean 등의 응답을 처리할 수 있도록 지원.

#### 🔹 Step 2: 인터넷 권한 추가 (`AndroidManifest.xml`)
안드로이드 앱이 네트워크를 사용하려면 인터넷 권한을 설정해야 함.

📌 **`AndroidManifest.xml`**
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

✅ **설명**
- 인터넷 권한을 추가하지 않으면 API 요청이 차단될 수 있음.

#### 🔹 Step 3: Retrofit API 인터페이스 생성
백엔드의 API와 통신할 **Retrofit 인터페이스**를 정의해야 함.

✅ **왜 필요한가?**
- Retrofit을 사용하면 **인터페이스만 정의하면 HTTP 요청을 자동으로 생성**하여 실행할 수 있음.
- `@POST`, `@Body` 등의 **어노테이션을 활용하면 코드가 간결하고 유지보수가 쉬움**.
- JSON 요청/응답을 **자동 변환**하여 데이터를 쉽게 주고받을 수 있음.
- **API가 추가될 경우, 인터페이스만 수정하면 되므로 확장성이 뛰어남**.

📌 **`AuthApiService.java`**
```java
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApiService {
    @POST("api/auth/signup")
    Call<Void> signup(@Body SignupRequest request);

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
```

✅ **설명**
- `@POST("api/auth/signup")` → 백엔드의 회원가입 API와 연결.
- `@POST("api/auth/login")` → 로그인 API와 연결.
- `@Body SignupRequest request` → 요청 데이터를 JSON 형식으로 전송.

#### 🔹 Step 4: Retrofit 클라이언트 설정
Retrofit을 사용하려면 **클라이언트를 생성하여 API 요청을 처리**해야 함.

📌 **환경 변수 설정 방법 (권장)**
1️⃣ **`gradle.properties` 파일에 BASE_URL 추가**
```properties
BASE_URL="http://192.168.1.100:8080/"
```

2️⃣ **`build.gradle (Module: app)`에서 설정**
```gradle
android {
    buildFeatures {
        buildConfig = true
    }
    
    defaultConfig {
        val baseUrl: String = project.findProperty("BASE_URL") as String? ?: "http://192.168.1.100:8080/"
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    }
}
```

> 현재 build.gradle.kts에서 buildConfigField를 추가했지만, Android Gradle Plugin 8.0 이상에서는 기본적으로 BuildConfig가 비활성화됨.
즉, buildConfigField를 사용하려면 명시적으로 buildConfig 기능을 활성화해야 함.

3️⃣ **`RetrofitClient.java`에서 사용**
```java
private static final String BASE_URL = BuildConfig.BASE_URL;
```
✅ **이제 환경 변수에서 BASE_URL을 관리할 수 있어 유지보수가 더 쉬워짐.**

📌 **`RetrofitClient.java`**
```java
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class RetrofitClient {
    private static final String BASE_URL = BuildConfig.BASE_URL; // 환경 변수에서 BASE_URL 불러오기

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 자동 변환
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
```

✅ **설명**
- `BASE_URL` → 백엔드(Spring Boot) 서버의 IP 주소 (`localhost`가 아닌 네트워크 IP 사용 필요).
- `OkHttpClient` → 네트워크 요청 로그 확인 가능.
- `GsonConverterFactory` → JSON 데이터를 Java 객체로 변환 지원.
- `getClient()`를 통해 **전역적으로 Retrofit 인스턴스를 관리**할 수 있음.

---

## DTO 설정

### 📌 DTO (Data Transfer Object) 설정

### 1️⃣ **DTO란?**
- DTO (Data Transfer Object) 는 **클라이언트와 서버 간 데이터를 주고받을 때 사용되는 객체**입니다.
- 네트워크 통신에서 JSON 데이터를 Java 객체로 변환하거나, Java 객체를 JSON으로 변환할 때 활용됩니다.
- Retrofit을 이용해 API 요청/응답 시 DTO를 정의하여 데이터의 구조를 명확히 할 수 있습니다.

### 2️⃣ **DTO 파일 생성 (Java) - Retrofit과 Gson 사용**
📌 **아래와 같은 요청/응답 DTO를 작성하여 API와 통신합니다.**

#### **🔹 `SignupRequest.java` (회원가입 요청 DTO)**
```java
import com.google.gson.annotations.SerializedName;

public class SignupRequest {
    @SerializedName("email")
    private final String email;
    @SerializedName("password")
    private final String password;
    @SerializedName("passwordCheck")
    private final String passwordCheck;
    @SerializedName("nickname")
    private final String nickname;

    public SignupRequest(String email, String password, String passwordCheck, String nickname) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", passwordCheck='" + passwordCheck + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPasswordCheck() { return passwordCheck; }
    public String getNickname() { return nickname; }
}
```
✅ **설명**  
- `@SerializedName("필드명")`을 사용하여 JSON 필드명을 Java 변수와 매핑.  
- `toString()` 메서드를 오버라이드하여 객체 정보를 쉽게 확인할 수 있도록 설정.  

#### **🔹 `LoginRequest.java` (로그인 요청 DTO)**
```java
import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    private final String email;
    @SerializedName("password")
    private final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
```
✅ **설명**  
- `email`, `password`를 JSON 요청으로 변환할 수 있도록 `@SerializedName`을 추가.  
- `toString()`을 추가하여 객체 정보 확인 가능.  

#### **🔹 `LoginResponse.java` (로그인 응답 DTO)**
```java
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("accessToken")
    private final String accessToken;
    @SerializedName("refreshToken")
    private final String refreshToken;

    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
}
```
✅ **설명**  
- 로그인 성공 시 서버에서 `accessToken`, `refreshToken`을 반환.  
- `@SerializedName`을 사용하여 JSON 응답과 Java 필드를 매핑.  
- `toString()`을 추가하여 디버깅 시 쉽게 확인 가능.  

### 3️⃣ **DTO 사용 예시**
📌 **회원가입 API 요청 예시**
```java
SignupRequest request = new SignupRequest("user@example.com", "password123", "password123", "nickname");
Call<Void> call = apiService.signup(request);
```

📌 **로그인 API 요청 예시**
```java
LoginRequest request = new LoginRequest("user@example.com", "password123");
Call<LoginResponse> call = apiService.login(request);
```

### ✅ **결론**
✔ **DTO를 사용하면 API 요청/응답 데이터를 명확하게 정의**할 수 있음.  
✔ **Retrofit을 사용할 때 DTO를 활용하여 JSON 변환을 자동화할 수 있음.**  
✔ **로그인, 회원가입 등 기능별로 DTO를 분리하면 유지보수가 쉬워짐.** 🚀  

---

## 회원가입 기능 구현 및 테스트

### 📌 회원가입 연결 및 테스트

### 🔹 1️⃣ 회원가입 기능 테스트 - 에뮬레이터 및 안드로이드 기기 테스트

📌 **테스트 절차**

1. **Android Emulator 또는 실제 안드로이드 기기를 실행**
2. **앱 실행 후 회원가입 화면으로 이동**
3. **이메일, 비밀번호, 닉네임 입력 후 가입 요청**
4. **서버 응답을 확인하고 회원가입 성공 여부 확인**
5. **성공 시 로그인 화면으로 이동하는지 확인**
6. **실패 시 적절한 오류 메시지가 표시되는지 확인**

📌 **네트워크 설정 및 BASE_URL 차이점**

- **에뮬레이터에서 테스트할 경우**: `BASE_URL`을 `10.0.2.2:8080`으로 설정하여 로컬 서버와 연결
- **실제 안드로이드 기기에서 테스트할 경우**: 같은 네트워크에 연결된 PC의 **로컬 IP 주소**(`192.168.xxx.xxx`)를 BASE_URL로 설정
- **환경 변수에서 BASE_URL을 불러와 관리 가능**

```java
// 에뮬레이터에서 테스트할 때
private static final String BASE_URL = "http://10.0.2.2:8080/";

// 실제 안드로이드 기기에서 테스트할 때 (PC IP 주소를 확인 후 변경 필요)
private static final String BASE_URL = "http://192.168.1.100:8080/"; 

// 환경 변수에서 BASE_URL을 불러오는 방식
private static final String BASE_URL = BuildConfig.BASE_URL; // 환경 변수에서 BASE_URL 불러오기
```

📌 **PC의 로컬 IP 주소 확인 방법 (Windows 기준)**
- 터미널(명령 프롬프트)에서 다음 명령 실행
```sh
ipconfig
```
- `IPv4 Address` 값을 확인하고 `BASE_URL`을 해당 IP로 변경

📌 **백엔드(Spring Boot)가 실행 중인지 확인 (`http://10.0.2.2:8080/h2-console` 또는 `http://192.168.1.100:8080/h2-console` 접속 가능 여부 체크)**

📌 **환경 변수에서 BASE_URL을 가져오는 이유**
- `BuildConfig.BASE_URL`을 사용하면 **환경별로 다른 BASE_URL을 설정 가능**
- 로컬 테스트 (`10.0.2.2:8080`), 사내 네트워크 (`192.168.xxx.xxx:8080`), 배포 서버 (예: `https://api.example.com`) 등을 동적으로 설정 가능
- `gradle.properties` 또는 `.env` 파일에서 BASE_URL을 관리하여 환경에 따라 쉽게 변경 가능

📌 **예시 (환경 변수 설정 방법)**
- `gradle.properties`
```properties
BASE_URL="http://192.168.1.100:8080/"
```
- `AndroidManifest.xml`
```xml
<manifest>
    <application>
        <meta-data
            android:name="BASE_URL"
            android:value="@string/base_url" />
    </application>
</manifest>
```

📌 **요청 데이터 예시 (회원가입 요청)**

```json
{
  "email": "test@example.com",
  "password": "password123",
  "passwordCheck": "password123",
  "nickname": "TestUser"
}
```

📌 **성공 응답 (HTTP 200)**

```json
{
  "message": "회원가입 성공!"
}
```

📌 **실패 응답 (HTTP 400 - 비밀번호 불일치 예시)**

```json
{
  "message": "비밀번호가 일치하지 않습니다."
}
```

---

### 🔹 2️⃣ 발생할 수 있는 오류 및 해결 방법

### 🚨 **오류 1: 회원가입 요청 실패 (400 Bad Request)**

📌 **원인:**

- `password`와 `passwordCheck`가 일치하지 않음
- `email`이 이미 존재하는 경우 (중복 이메일)
- 입력값이 비어 있음
- 닉네임이 2자 이상 12자 이하가 아님
- 닉네임에 특수문자가 포함됨

📌 **해결 방법:**

- 회원가입 전 **비밀번호 확인 로직 추가**
- 서버에서 `email` 중복 검사 후 **적절한 오류 메시지 표시**
- 빈 입력값 확인 후 **`Toast` 메시지 출력**
- 닉네임 길이 및 특수문자 포함 여부 검사

✔ **수정 코드 예시 (`SignupActivity.java`)**

```java
if (email.isEmpty() || password.isEmpty() || passwordCheck.isEmpty() || nickname.isEmpty()) {
    Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
    return;
}

if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
    Toast.makeText(this, "올바른 이메일 형식을 입력하세요.", Toast.LENGTH_SHORT).show();
    return;
}
```

---

### 🚨 **오류 2: CLEARTEXT communication not permitted 오류 발생**

📌 **원인:**
- Android API 28 이상에서는 기본적으로 `HTTP`(암호화되지 않은 통신)를 차단함.
- `http://192.168.1.100:8080` 같은 **HTTP 요청이 허용되지 않음**.

📌 **해결 방법:**
1️⃣ **`res/xml/network_security_config.xml` 파일 생성**

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">192.168.1.100</domain>
        <domain includeSubdomains="true">10.0.2.2</domain> <!-- 에뮬레이터용 추가 -->
    </domain-config>
</network-security-config>
```

2️⃣ **`AndroidManifest.xml`에 네트워크 보안 설정 추가**

```xml
<application
    android:usesCleartextTraffic="true"
    android:networkSecurityConfig="@xml/network_security_config"
    ... >
</application>
```

---

## 로그인 기능 구현

### 🔹 1️⃣ 로그인 기능 개요

1. 사용자가 이메일과 비밀번호를 입력하여 로그인 (`LoginRequest`(email, password))
2. 로그인 성공 시 **Access Token & Refresh Token** (`LoginResponse`(accessToken, refreshToken)) 을 반환받아 저장
3. 이후 API 요청 시 Access Token을 인증 헤더에 포함하여 인증 처리
4. Access Token 만료 시 Refresh Token을 이용해 새로운 Access Token 발급 요청

---

### 🔹 2️⃣ Retrofit을 사용한 로그인 요청

📌 **로그인 버튼 클릭 시 실행할 코드** (`LoginActivity.java`)

```java
public void onLoginButtonClick(View view) {
    String email = etEmail.getText().toString().trim();
    String password = etPassword.getText().toString().trim();

    if (email.isEmpty() || password.isEmpty()) {
        Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
        return;
    }

    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
    Call<LoginResponse> call = apiService.login(new LoginRequest(email, password));

    call.enqueue(new Callback<LoginResponse>() {
        @Override
        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                LoginResponse loginResponse = response.body();
                saveTokens(loginResponse.getAccessToken(), loginResponse.getRefreshToken());
                Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "로그인 실패: " + response.message(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<LoginResponse> call, Throwable t) {
            Toast.makeText(LoginActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
        }
    });
}
```

1. 로그인 성공 시 토큰을 저장하고 `MainActivity`로 이동
2. 실패 시 Toast 메시지로 실패 이유 표시
3. `saveTokens()` 함수에서 **Access Token & Refresh Token을 저장**

---

### 🔹 3️⃣ Access Token & Refresh Token 저장

📌 로그인 성공 후 받은 토큰을 `SharedPreferences`에 저장

```java
private void saveTokens(String accessToken, String refreshToken) {
    SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString("accessToken", accessToken);
    editor.putString("refreshToken", refreshToken);
    editor.apply();
}
```

---

## JWT 토큰 해석 및 캐싱 방식

### 🔹 **1️⃣ JWT 토큰 활용 개요**
✔ 로그인 성공 후, **서버에서 받은 Access Token과 Refresh Token을 저장**  
✔ API 요청 시 **Access Token을 헤더에 추가하여 인증**  
✔ Access Token이 만료되면 **Refresh Token을 사용하여 새로운 Access Token을 요청**  
✔ 필요할 경우 **토큰을 디코딩하여 사용자 정보 확인**  

---

### 🔹 **2️⃣ JJWT vs Base64 방식 비교**
📌 JJWT(Jackson 기반 JWT 라이브러리)와 Base64 디코딩 방식을 비교하여 각 방법의 장단점을 분석함.  

#### **✅ JJWT (Java JWT 라이브러리)**
| 장점 | 단점 |
|------|------|
| JWT 서명(Signature) 검증 가능 | 별도의 라이브러리 필요 |
| 간편한 API 제공 (`Jwts.parserBuilder()`) | 앱 크기가 약간 증가할 수 있음 |
| JWT 토큰 생성 및 검증 기능 제공 | |

#### **✅ Base64 디코딩 방식**
| 장점 | 단점 |
|------|------|
| 라이브러리 없이 간단하게 사용 가능 | JWT 서명(Signature) 검증 불가능 |
| 빠르고 가벼움 | 보안적으로 취약할 수 있음 (서버에서 검증 필요) |

**⚠️ 주의:** Base64 방식은 토큰을 해석하는 용도로만 사용하고, 보안 검증은 서버에서 수행해야 함.

---

### 🔹 **3️⃣ Base64를 이용한 JWT 디코딩 방법**
📌 **JWT 토큰을 Base64로 디코딩하여 유저 정보를 확인하는 방법**  

✅ **JWT 구조 (Base64 인코딩된 문자열)**
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
.
eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwibmlja25hbWUiOiJUZXN0VXNlciJ9
.
sdfjKJfLKJSDFLJLSDFLKJSDLFJ
```
- **1번째 부분 (Header)** → 암호화 알고리즘 정보
- **2번째 부분 (Payload, 유저 정보)** → 우리가 해석해야 할 부분
- **3번째 부분 (Signature, 서명)** → 보안 검증 용도로, 클라이언트에서 확인할 필요 없음

✅ **Base64 디코딩하여 JWT Payload 확인**
```java
import android.util.Base64;
import org.json.JSONObject;

public class JwtDecoder {
    public static JSONObject decodeJwtPayload(String jwtToken) {
        try {
            // JWT는 '.'으로 구분되므로, 두 번째 부분(Payload)을 가져옴
            String[] splitToken = jwtToken.split("\\.");
            if (splitToken.length < 2) return null;

            String payload = splitToken[1];

            // Base64 디코딩
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE);
            String decodedString = new String(decodedBytes);

            // JSON으로 변환하여 반환
            return new JSONObject(decodedString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
```

✅ **사용 예시**
```java
String accessToken = TokenManager.getAccessToken();
JSONObject userInfo = JwtDecoder.decodeJwtPayload(accessToken);

if (userInfo != null) {
    String email = userInfo.optString("sub"); // JWT 'sub' 필드 (이메일)
    String nickname = userInfo.optString("nickname"); // 닉네임 정보

    System.out.println("Email: " + email);
    System.out.println("Nickname: " + nickname);
}
```
✔ JWT의 Payload에서 `sub`, `nickname` 같은 데이터를 꺼내서 활용 가능!  

✅ **JWT 만료 시간(`exp`) 확인 방법**
```java
public static boolean isTokenExpired(String jwtToken) {
    try {
        JSONObject payload = decodeJwtPayload(jwtToken);
        if (payload == null) return true;

        long exp = payload.optLong("exp", 0);
        long currentTime = System.currentTimeMillis() / 1000;

        return exp < currentTime;
    } catch (Exception e) {
        e.printStackTrace();
        return true;
    }
}
```

✅ **만료 여부 확인**
```java
boolean isExpired = JwtDecoder.isTokenExpired(accessToken);
if (isExpired) {
    System.out.println("토큰이 만료됨. 다시 로그인 필요!");
} else {
    System.out.println("토큰이 유효함!");
}
```

---

### 🔹 **4️⃣ API 요청 시 Access Token 포함하기**
📌 **Retrofit을 이용하여 API 요청 시 Access Token 추가**  
✅ **OkHttpClient를 활용한 토큰 자동 추가**  
```java
public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String accessToken = TokenManager.getAccessToken(); // ✅ TokenManager에서 가져옴

        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer " + accessToken); // ✅ 헤더에 추가

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
```

---

## JWT 기반 API 요청 및 자동 갱신 구현

### 🔹 **1️⃣ Refresh Token을 이용한 Access Token 자동 갱신**
📌 Refresh Token을 이용해 Access Token을 자동 갱신하는 기능을 추가해야 함.  

✅ **작업할 내용**
- API 요청 시 **Access Token이 만료되었을 경우, 저장된 Refresh Token으로 새로운 Access Token을 요청**
- 새로운 Access Token을 **저장 후 원래 요청을 재시도**하도록 처리
- Refresh Token이 만료된 경우 **로그아웃 처리** 및 **다시 로그인 요청**

✅ **수정된 `AuthInterceptor` 코드 (토큰 만료 시 자동 갱신)**
```java
public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String accessToken = TokenManager.getAccessToken();
        Request original = chain.request();
        Request request = original.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
        
        Response response = chain.proceed(request);
        
        // 401 Unauthorized → Access Token이 만료되었을 가능성이 있음
        if (response.code() == 401) {
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
                    }
                }
            }
        }
        return response;
    }
}
```

### 🔹 AuthInterceptor가 동작하는 시점 분석

#### 📌 `AuthInterceptor`는 언제 동작하는가?
`AuthInterceptor`는 **Retrofit을 통해 네트워크 요청을 보낼 때마다 자동으로 실행되는 인터셉터**(Interceptor)야.
즉, **모든 API 요청 전에 Access Token을 헤더에 추가하고, 만료된 경우 Refresh Token을 이용해 갱신하는 역할**을 함.

### 📌 앱 실행 순서 & AuthInterceptor의 동작 위치
#### 1️⃣ 앱 실행 (SplashScreen)
- `TokenManager.getAccessToken()`을 호출하여 저장된 Access Token을 가져옴.
    - Access Token이 없으면 → `LoginActivity`로 이동
    - Access Token이 있으면 → `MainActivity`로 이동

#### 2️⃣ MainActivity에서 API 요청
- 이때 Retrofit을 통해 API 요청을 보냄 → `AuthInterceptor` 실행
- `AuthInterceptor`에서 저장된 Access Token을 헤더에 추가
- 서버에서 응답을 받음
    - 정상 응답 (200 OK) → API 데이터 사용
    - 401 Unauthorized (Access Token 만료됨) → `AuthInterceptor`가 자동으로 Refresh Token을 사용해 새로운 Access Token 요청
        - 새로운 Access Token 발급 후 기존 요청을 다시 실행
        - Refresh Token까지 만료됨 → 로그아웃 처리 (LoginActivity 이동)

✅ **새로운 Access Token 갱신 메서드 (`TokenManager` 수정)**
```java
public static String refreshAccessToken(String refreshToken) {
    try {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Response<LoginResponse> response = apiService.refreshToken(refreshToken).execute();
        
        if (response.isSuccessful() && response.body() != null) {
            return response.body().getAccessToken();
        }
    } catch (IOException e) {
        Log.e("AuthInterceptor", "Access Token 재발급 중 오류 발생", e);
    }
    return null;
}
```

---

### 🔹 **2️⃣ API 요청 테스트**
📌 위의 작업이 끝나면, **실제로 API 요청이 정상적으로 동작하는지 테스트해야 함.**  

✅ **테스트할 시나리오**
1. **Access Token이 유효할 때 정상 요청이 가능한지 확인**
2. **Access Token이 만료되었을 때 자동으로 Refresh Token을 사용해 갱신되는지 확인**
3. **Refresh Token까지 만료된 경우 로그아웃되는지 확인**

✅ **테스트 요청 예시 (`MainActivity`)**
```java
ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
Call<UserResponse> call = apiService.getUserInfo();
call.enqueue(new Callback<UserResponse>() {
    @Override
    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
        if (response.isSuccessful()) {
            System.out.println("✅ API 요청 성공: " + response.body().toString());
        } else if (response.code() == 401) {
            System.out.println("⚠️ Access Token 만료됨, 자동 갱신 시도");
        }
    }
    
    @Override
    public void onFailure(Call<UserResponse> call, Throwable t) {
        System.out.println("❌ API 요청 실패: " + t.getMessage());
    }
});
```

---

## 하단 바 Navigation

### 📌 **앱 하단 바(Navigation Bar) 추가**

### 🔹 **1️⃣ 프로젝트 개요**

✅ 하단 바(Navigation Bar)를 추가하여 **앱의 주요 화면 간 빠르게 이동**할 수 있도록 함.  
✅ 현재 팀원들이 각자 **액티비티(Activity) 기반**으로 개발하고 있으므로, 초기에는 액티비티 기반으로 하단 바를 구현.  
✅ 추후 유지보수를 고려하여 **프래그먼트(Fragment) 기반으로 전환하는 것도 가능**.  

### 🔹 **2️⃣ 하단 바(Navigation Bar) 추가 방법**

#### ✅ `activity_main.xml`에서 `BottomNavigationView` 추가

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!-- 하단 네비게이션 바 -->
    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottomNavigationView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        app:menu="@menu/bottom_nav_menu"/>
</RelativeLayout>
```

#### ✅ `res/menu/bottom_nav_menu.xml` 생성 (네비게이션 메뉴 정의)

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/navigation_calendar"
        android:icon="@drawable/icon_calendar_48dp"
        android:title="@string/nav_calendar"/>
    
    <item
        android:id="@+id/navigation_wallet"
        android:icon="@drawable/icon_wallet_48dp"
        android:title="@string/nav_wallet"/>

    <item
        android:id="@+id/navigation_group"
        android:icon="@drawable/icon_group_48dp"
        android:title="@string/nav_group"/>

    <item
        android:id="@+id/navigation_settings"
        android:icon="@drawable/icon_settings_48dp"
        android:title="@string/nav_settings"/>
</menu>
```

#### ✅ `res/values/strings.xml`에 네비게이션 타이틀 추가

```xml
<string name="nav_calendar">캘린더</string>
<string name="nav_wallet">가계부</string>
<string name="nav_group">그룹</string>
<string name="nav_settings">설정</string>
```

#### ✅ `NavigationHandler.java` 클래스 추가 (네비게이션 이벤트 처리)

```java
public class NavigationHandler {
    public static boolean handleNavigation(Context context, MenuItem item) {
        Intent intent = null;
        int itemId = item.getItemId(); // ID를 가져옴

        if (itemId == R.id.navigation_calendar) {
            if (!(context instanceof CalendarActivity)) {
                intent = new Intent(context, CalendarActivity.class);
            }
        } else if (itemId == R.id.navigation_wallet) {
            if (!(context instanceof WalletActivity)) {
                intent = new Intent(context, WalletActivity.class);
            }
        } else if (itemId == R.id.navigation_group) {
            if (!(context instanceof GroupActivity)) {
                intent = new Intent(context, GroupActivity.class);
            }
        } else if (itemId == R.id.navigation_settings) {
            if (!(context instanceof SettingsActivity)) {
                intent = new Intent(context, SettingsActivity.class);
            }
        }

        if (intent != null) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}
```

📌 현재 액티비티와 이동할 액티비티가 동일하면 새로 실행하지 않도록 **중복 실행 방지 처리 추가**

#### ✅ `MainActivity.java`에서 BottomNavigationView 설정 (액티비티 기반)

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> NavigationHandler.handleNavigation(this, item));
    }
}
```

### 🔹 **3️⃣ 추후 프래그먼트로 전환 고려**

✅ 현재는 **각 탭을 별도의 액티비티(Activity)로 이동하는 방식**으로 구현.  
✅ 추후 유지보수 및 네비게이션 최적화를 위해 **프래그먼트(Fragment) 기반으로 변경 가능**.  
✅ 프래그먼트 전환 방식으로 변경하면 **하단 바가 유지되면서 화면이 부드럽게 전환됨**.  

📌 **현재는 액티비티 방식 유지, 프로젝트가 안정되면 프래그먼트로 전환 고려!** 🚀

---

### 🔹 **4️⃣ 홈 화면을 캘린더로 설정**

✅ 앱을 실행하면 홈 화면을 **캘린더(CalendarActivity)로 설정.**  
✅ `MainActivity` 대신 `CalendarActivity`가 앱의 시작점이 되도록 변경.  
✅ `NavigationHandler`에서 **현재 액티비티 중복 실행 방지 코드 추가**.  

#### ✅ `AndroidManifest.xml`에서 `MainActivity`를 `CalendarActivity`로 변경

```xml
<activity
    android:name=".CalendarActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

📌 `android.intent.action.MAIN`과 `android.intent.category.LAUNCHER`를 `CalendarActivity`로 설정하여 앱 실행 시 캘린더 화면이 먼저 보이도록 함.

### 🔹 5️⃣ 네비게이션 바 관련 문제 해결

#### ✅ **BottomNavigation 클릭 시 애니메이션 제거**
**문제:**
- BottomNavigationView를 클릭하면 새로운 Activity가 생성될 때 오른쪽으로 넘어가는 애니메이션이 발생함.
- 또한, 새로운 Activity가 계속 생성되면서 기존의 Activity가 쌓이는 문제가 있음.

**해결 방법:**
1. **애니메이션 제거:** `overridePendingTransition(0, 0);` 추가하여 애니메이션을 비활성화함.
2. **중복 실행 방지:** 현재 액티비티와 이동할 액티비티가 다를 경우에만 `startActivity()` 실행.

```java
public class NavigationHandler {
    public static boolean handleNavigation(Context context, MenuItem item) {
        Intent intent = null;
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_calendar && !(context instanceof CalendarActivity)) {
            intent = new Intent(context, CalendarActivity.class);
        } else if (itemId == R.id.navigation_wallet && !(context instanceof WalletActivity)) {
            intent = new Intent(context, WalletActivity.class);
        } else if (itemId == R.id.navigation_group && !(context instanceof GroupActivity)) {
            intent = new Intent(context, GroupActivity.class);
        } else if (itemId == R.id.navigation_settings && !(context instanceof SettingsActivity)) {
            intent = new Intent(context, SettingsActivity.class);
        }

        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(0, 0); // ✅ 애니메이션 제거
            return true;
        }
        return false;
    }
}
```

#### ✅ 다시 `MainActivity` 사용
**문제:**
- 캘린더를 누르면 Splash 화면이 순간적으로 나타났다가 사라짐

**해결 방법:**
- `MainActivity` 에서 기본적인 세팅(Login 및 UserSession 관련) 후 `CalendarActivity` 로 넘어가는 방식으로 재구현

#### **📌 `onResume()`에서 `bottomNavigationView`를 이렇게 설정해야 하는 이유**

```java
@Override
protected void onResume() {
    super.onResume();
    bottomNavigationView.setOnItemSelectedListener(null);
    bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
    bottomNavigationView.setOnItemSelectedListener(item -> NavigationHandler.handleNavigation(this, item));
}
```

---

#### **🚀 동작 방식과 필요한 이유**

1️⃣ **`setOnItemSelectedListener(null);` → 리스너 제거**
   - `setSelectedItemId()`를 호출하면 **`setOnItemSelectedListener`가 트리거될 수 있음.**
   - 즉, **네비게이션 바의 선택 상태를 변경할 때 `onItemSelected()`가 실행되지 않도록 하기 위해 리스너를 제거**.

2️⃣ **`setSelectedItemId(R.id.navigation_settings);` → 현재 화면을 선택된 상태로 설정**
   - `onResume()`이 실행될 때 **현재 액티비티가 선택되었음을 하단 바에 반영**.
   - 사용자가 뒤로 가기를 할 때, **올바른 탭이 활성화되도록 보장**.

3️⃣ **`setOnItemSelectedListener(item -> NavigationHandler.handleNavigation(this, item));` → 리스너 재설정**
   - `setSelectedItemId()`를 설정한 후, 다시 네비게이션 이벤트를 처리하기 위해 리스너를 추가.
   - **이제 네비게이션 아이템을 클릭하면 `NavigationHandler.handleNavigation()`이 정상적으로 실행됨**.

---

#### **🔍 왜 이런 처리가 필요한가?**
- 만약 **리스너를 제거하지 않고 `setSelectedItemId()`를 실행하면**, 네비게이션 변경 이벤트가 다시 발생할 수 있음.
- `onResume()`에서 `setSelectedItemId()`를 적용할 때 **예기치 않은 네비게이션 이동을 방지하려면** 반드시 **리스너를 일시적으로 제거**해야 함.
- 다시 **리스너를 추가해야 하단 네비게이션이 정상적으로 작동**함.

---

### **✅ 액티비티 뒤로가기 애니메이션 지우는 방법: `OnBackPressedDispatcher` 사용하기**
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    // 뒤로가기 이벤트 처리
    OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
    dispatcher.addCallback(this, new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            finish();
            overridePendingTransition(0, 0); // 애니메이션 제거
        }
    });
}
```

### **📌 주요 변경점**
1. `getOnBackPressedDispatcher()` → **새로운 뒤로가기 이벤트 처리 시스템 사용**
2. `OnBackPressedCallback` → **뒤로 가기 동작을 커스텀할 수 있음**
3. `overridePendingTransition(0, 0);` → **애니메이션 없이 액티비티 종료**

---

## Settings Activity 구성

### 📌 **설정(Settings) 구현**  
설정 화면을 보다 효율적으로 구성하기 위해 **`SettingsActivity`에서 직접 설정 목록을 관리**하도록 설계하였습니다. 이를 통해 **네비게이션 바를 유지하면서도 설정 목록을 쉽게 확장 및 관리**할 수 있습니다.

---

## 🔹 설정 목록 UI 및 기본 구조

✅ **설정 화면을 `SettingsActivity`에서 직접 관리**  
✅ **RecyclerView**를 활용하여 설정 목록을 동적으로 표시  
✅ 클릭 시 해당 설정 화면(Activity)으로 이동  
✅ 상단에는 **사용자 프로필(이미지, 이름, 이메일) 표시**  
✅ 하단에는 **네비게이션 바 유지**하여 다른 화면으로 이동 가능  
✅ 설정 목록을 쉽게 확장 가능하도록 설계  
 
### **📌 `SettingsActivity.java` 생성 (설정 화면 컨테이너 역할)**  
> `SettingsActivity`에서 **설정 목록과 사용자 프로필을 직접 관리**하여 하나의 화면에서 모든 설정을 조작할 수 있도록 설계. `RecyclerView`를 활용해 설정 항목을 동적으로 표시하며, 클릭 시 해당 설정 화면(Activity)으로 이동하도록 구성.

```java
public class SettingsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerViewSettings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadSettings();
    }

    private void loadSettings() {
        List<SettingsItem> settingsList = new ArrayList<>();

        settingsList.add(new SettingsItem("프로필 수정", R.drawable.icon_profile_48dp));
        settingsList.add(new SettingsItem("비밀번호 변경", R.drawable.icon_password_48dp));
        settingsList.add(new SettingsItem("푸시 알림 설정", R.drawable.icon_notifications_48dp));
        settingsList.add(new SettingsItem("로그아웃", R.drawable.icon_logout_48dp));

        SettingsAdapter adapter = new SettingsAdapter(settingsList, this);
        recyclerView.setAdapter(adapter);
    }
}
```

### **📌 `SettingsAdapter.java` 생성 (RecyclerView Adapter)**
> `RecyclerView` 클릭 시 적절한 설정 화면으로 이동하도록 구현

```java
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.ViewHolder> {
    private final List<SettingsItem> settingsList;
    private final Context context;

    public SettingsAdapter(List<SettingsItem> settingsList, Context context) {
        this.settingsList = settingsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SettingsItem item = settingsList.get(position);
        holder.icon.setImageResource(item.getIconResId());
        holder.title.setText(item.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = null;
            switch (item.getTitle()) {
                case "프로필 수정":
                    intent = new Intent(context, ProfileEditActivity.class);
                    break;
                case "비밀번호 변경":
                    intent = new Intent(context, PasswordChangeActivity.class);
                    break;
                case "푸시 알림 설정":
                    intent = new Intent(context, NotificationSettingsActivity.class);
                    break;
                case "로그아웃":
                    TokenManager.clearTokens(); // 로그아웃 처리
                    intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // 백스택 제거
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    break;
            }
            if (intent != null) context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.settingsIcon);
            title = itemView.findViewById(R.id.settingsTitle);
        }
    }
}
```
#### ✨ 추가 설명: `Intent.FLAG_ACTIVITY_CLEAR_TASK` 사용 이유
단순히 `finish()`만 하면 `SettingsActivity`만 종료되지만,
`FLAG_ACTIVITY_CLEAR_TASK`를 추가하면 앱의 백스택을 완전히 초기화하여
뒤로 가기 버튼을 눌러도 설정 화면으로 돌아가지 않음.

---

## Settings 화면 구성

### 📌 UserSession을 활용한 MainActivity 수정

#### ✅ **수정 내용**
- **기존 코드에서는 Access Token만 확인**하고 로그인 여부를 결정.
- **UserSession을 추가하여** 저장된 토큰을 해석하고, 유저 정보를 캐싱하도록 개선.
- 앱 실행 시 `UserSession`을 초기화하여 **유저 정보(JSON 데이터)를 전역적으로 활용 가능**하게 변경.

---

### 🔹 **수정된 MainActivity.java**
```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // SplashScreen API 적용
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        // TokenManager 초기화 (앱 실행 시 한 번만 호출)
        TokenManager.init(this);

        // SharedPreferences에서 저장된 Access Token 확인
        String accessToken = TokenManager.getAccessToken();

        // Access Token 존재하지 않을 때 → 로그인 화면으로 이동
        if (accessToken == null || accessToken.isEmpty()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return; // 이후 코드 실행 방지
        }

        // ✅ UserSession 초기화 → JWT 해석하여 유저 정보 저장
        UserSession.setAccessToken(accessToken);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```

`SettingsActivity`에서 `UserSession`을 활용하여 유저 정보를 가져와서 프로필 이미지, 닉네임, 이메일을 설정하는 부분을 추가해 주었습니다.

### 🔹 수정된 `SettingsActivity.java`
```java
public class SettingsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView profileImage;
    private TextView profileName, profileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // UI 요소 초기화
        profileImage = findViewById(R.id.profileImage);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerViewSettings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 유저 정보 설정
        loadUserInfo();

        // 설정 목록 로드
        loadSettings();
    }

    private void loadUserInfo() {
        JSONObject userClaims = UserSession.getUserClaims();
        if (userClaims != null) {
            try {
                String nickname = userClaims.getString("nickname");
                String email = userClaims.getString("email");
                String imageUrl = userClaims.getString("imageUrl");

                profileName.setText(nickname);
                profileEmail.setText(email);

                // Glide를 사용하여 프로필 이미지 로드
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_profile_placeholder) // 기본 이미지
                        .error(R.drawable.ic_profile_placeholder) // 오류 시 기본 이미지
                        .circleCrop() // 원형으로 자르기
                        .into(profileImage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadSettings() {
        List<SettingsItem> settingsList = new ArrayList<>();

        settingsList.add(new SettingsItem("프로필 수정", R.drawable.icon_profile_48dp));
        settingsList.add(new SettingsItem("비밀번호 변경", R.drawable.icon_password_48dp));
        settingsList.add(new SettingsItem("푸시 알림 설정", R.drawable.icon_notifications_48dp));
        settingsList.add(new SettingsItem("로그아웃", R.drawable.icon_logout_48dp));

        SettingsAdapter adapter = new SettingsAdapter(settingsList, this);
        recyclerView.setAdapter(adapter);
    }
}
```

### 🔹 UI 미리보기
아래 이미지는 설정 화면의 디자인을 보여줍니다.  
<img src="./img/page_settings.jpg" width="200" height="400" /> <br />

---

### 📌 추가 설명
✅ **`UserSession.getUserClaims()`** 에서 유저 정보를 가져와 닉네임, 이메일, 프로필 이미지를 설정  
✅ **`Glide` 라이브러리**를 사용하여 `imageUrl`을 로드하고 프로필 이미지를 표시  
✅ **오류 발생 시 기본 프로필 이미지를 표시하도록 설정**  
✅ 기존의 `SettingsActivity`에서 유저 정보를 불러오는 로직을 추가하여 더욱 완성된 화면 제공  

---

## Settings - 프로필 수정 페이지

### 1️⃣ 개요

사용자가 자신의 프로필 정보를 수정할 수 있는 `ProfileEditActivity`를 구현합니다. 프로필 사진 변경, 닉네임 수정, 이메일 확인 등의 기능을 제공합니다.

### 2️⃣ 주요 기능

- ✅ **프로필 사진 변경**: 갤러리에서 선택하거나 기본 이미지로 변경
- ✅ **이미지 URL 입력**: 사용자가 URL을 입력하여 프로필 사진을 변경 가능
- ✅ **닉네임 수정**: 사용자가 닉네임을 입력하고 저장 가능
- ✅ **이메일 확인**: 변경 불가능한 읽기 전용 필드
- ✅ **저장 버튼**: 변경된 정보를 서버에 저장

### 3️⃣ 코드 구현

#### `ProfileEditActivity.java` 

```java
public class ProfileEditActivity extends AppCompatActivity {
    private ImageView profileImage;
    private EditText imageUrlInput, nicknameInput;
    private TextView emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_edit_profile);

        profileImage = findViewById(R.id.profileImage);
        imageUrlInput = findViewById(R.id.imageUrlInput);
        nicknameInput = findViewById(R.id.nicknameInput);
        emailText = findViewById(R.id.emailText);

        // 유저 정보 설정
        loadUserInfo();

        imageUrlInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력된 텍스트가 변경될 때 실행 (Glide로 이미지 업데이트)
                loadProfileImage(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * 사용자 정보를 불러와 UI에 설정하는 메서드
     */
    private void loadUserInfo() {
        JSONObject userClaims = UserSession.getUserClaims();

        try {
            String nickname = userClaims.getString("nickname");
            String email = userClaims.getString("sub");
            String imageUrl = userClaims.optString("imageUrl", "");

            nicknameInput.setText(nickname);
            emailText.setText(email);
            imageUrlInput.setText(imageUrl);

            // Glide를 사용하여 프로필 이미지 로드
            loadProfileImage(imageUrl);
        } catch (JSONException e) {
            Log.e("UserSession", "사용자 정보 JSON 파싱 오류: " + e.getMessage(), e);
        }
    }

    /**
     * Glide를 사용하여 프로필 이미지를 로드하는 메서드
     * @param imageUrl 이미지 URL
     */
    private void loadProfileImage(String imageUrl) {
        Glide.with(this)
                .load(imageUrl.isEmpty() ? R.drawable.icon_profile_placeholder : imageUrl)
                .placeholder(R.drawable.icon_profile_placeholder)
                .error(R.drawable.icon_profile_placeholder)
                .circleCrop()
                .into(profileImage);
    }

    /**
     * 뒤로가기 버튼 클릭 이벤트 핸들러
     * @param view 클릭된 뷰
     */
    public void onBackButtonClick(View view) {
        finish();
    }

    /**
     * 저장 버튼 클릭 이벤트 핸들러
     * @param view 클릭된 뷰
     */
    public void onSaveButtonClick(View view) {
        // 저장 로직 추가 예정
    }
}
```

### 4️⃣ UI 레이아웃 설계 (ConstraintLayout 적용)

#### `activity_settings_edit_profile.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/white"
    android:paddingVertical="24dp"
    android:paddingHorizontal="24dp"
    tools:context=".settings.activity.ProfileEditActivity">

    <!-- 뒤로가기 버튼 -->
    <ImageButton
        android:id="@+id/backButton"
        android:layout_width="24dp"
        android:layout_height="24dp"
        android:scaleType="fitCenter"
        android:src="@drawable/icon_arrow_back_48dp"
        android:contentDescription="@string/btn_back"
        android:background="@color/white"
        android:onClick="onBackButtonClick"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>

    <!-- 프로필 이미지 -->
    <ImageView
        android:id="@+id/profileImage"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:contentDescription="@string/profile_image_url"
        app:layout_constraintTop_toBottomOf="@id/backButton"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

    <!-- 이미지 URL 라벨 -->
    <TextView
        android:id="@+id/imageUrlLabel"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/profile_image_url"
        app:layout_constraintTop_toBottomOf="@id/profileImage"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="16dp"/>

    <!-- 이미지 URL 입력 -->
    <EditText
        android:id="@+id/imageUrlInput"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:hint="@string/profile_input_image_url"
        android:importantForAutofill="no"
        android:inputType="textUri"
        app:layout_constraintTop_toBottomOf="@id/imageUrlLabel"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="8dp"/>

    <!-- 닉네임 라벨 -->
    <TextView
        android:id="@+id/nicknameLabel"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/profile_nickname"
        app:layout_constraintTop_toBottomOf="@id/imageUrlInput"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="16dp"/>

    <!-- 닉네임 입력 -->
    <EditText
        android:id="@+id/nicknameInput"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:hint="@string/profile_input_nickname"
        android:importantForAutofill="no"
        android:inputType="text"
        app:layout_constraintTop_toBottomOf="@id/nicknameLabel"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="8dp"/>

    <!-- 이메일 라벨 -->
    <TextView
        android:id="@+id/emailLabel"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/profile_email"
        app:layout_constraintTop_toBottomOf="@id/nicknameInput"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="16dp"/>

    <!-- 이메일 텍스트 (읽기 전용) -->
    <TextView
        android:id="@+id/emailText"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@string/profile_input_email"
        android:textSize="16sp"
        app:layout_constraintTop_toBottomOf="@id/emailLabel"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="8dp"/>

    <!-- 저장 버튼 -->
    <Button
        android:id="@+id/saveButton"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@string/btn_save"
        android:onClick="onSaveButtonClick"
        app:layout_constraintTop_toBottomOf="@id/emailText"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="16dp"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 5️⃣ UI 미리보기

아래 이미지는 프로필 편집 화면의 디자인을 보여줍니다.

<img src="./img/page_settings_edit_profile.jpg" width="200" height="400" /> <br />

---

## 프로필 수정 기능 구현

### 1️⃣ 저장 버튼 클릭 이벤트 핸들러
```java
public void onSaveButtonClick(View view) {
    // 입력된 닉네임과 이미지 URL 가져오기
    String newNickname = nicknameInput.getText().toString().trim();
    String newImageUrl = imageUrlInput.getText().toString().trim();

    // 유효성 검사
    if (newNickname.isEmpty()) {
        Toast.makeText(this, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();
        return;
    }

    if (!newNickname.matches("^[a-zA-Z0-9가-힣]+$")) {
        Toast.makeText(this, "닉네임은 영문, 숫자, 한글만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
        return;
    }

    if (newNickname.length() < 2 || newNickname.length() > 12) {
        Toast.makeText(this, "닉네임은 2자 이상 12자 이하이어야 합니다.", Toast.LENGTH_SHORT).show();
        return;
    }

    // API 요청 객체 생성
    ProfileEditRequest request = new ProfileEditRequest(newNickname, newImageUrl);
        
    // API 요청 실행
    SettingsApiService apiService = RetrofitClient.getClient().create(SettingsApiService.class);
    Call<ProfileEditResponse> call = apiService.updateProfile(request);

    call.enqueue(new Callback<ProfileEditResponse>() {
        @Override
        public void onResponse(@NonNull Call<ProfileEditResponse> call, @NonNull Response<ProfileEditResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                Toast.makeText(ProfileEditActivity.this, "프로필이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ProfileEditActivity.this, "프로필 수정 실패", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(@NonNull Call<ProfileEditResponse> call, @NonNull Throwable t) {
            Toast.makeText(ProfileEditActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
        }
    });
}
```

---

### 2️⃣ DTO (Data Transfer Object) 설계
#### **`ProfileEditRequest.java`**
```java
import com.google.gson.annotations.SerializedName;

public class ProfileEditRequest {
    @SerializedName("nickname")
    private final String nickname;
    
    @SerializedName("imageUrl")
    private final String imageUrl;

    public ProfileEditRequest(String nickname, String imageUrl) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "ProfileEditRequest{" +
                "nickname='" + nickname + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
```

#### **`ProfileEditResponse.java`**
```java
import com.google.gson.annotations.SerializedName;

public class ProfileEditResponse {
    @SerializedName("accessToken")
    private final String accessToken;

    public ProfileEditResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @NonNull
    @Override
    public String toString() {
        return "ProfileEditResponse{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
```

---

### 3️⃣ 네트워크 API 인터페이스 설계
#### **`SettingsApiService.java`**
```java
public interface SettingsApiService {
    @PATCH("/api/profile")
    Call<ProfileEditResponse> updateProfile(@Body ProfileEditRequest request);
}
```

---

### 4️⃣ SettingsActivity의 `onResume()` 수정
```java
@Override
protected void onResume() {
    super.onResume();
    bottomNavigationView.setOnItemSelectedListener(null);
    bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
    bottomNavigationView.setOnItemSelectedListener(item -> NavigationHandler.handleNavigation(this, item));

    loadUserInfo(); // ✅ 유저 정보 다시 불러오기
}
```

---

### 🚀 기능 요약
✅ **저장 버튼 클릭 시 닉네임과 이미지 URL을 가져와 유효성 검사 후 API 요청 실행**  
✅ **Retrofit을 사용하여 `ProfileEditRequest`를 서버에 전송하고 `ProfileEditResponse`를 수신**  
✅ **API 성공 시 `AccessToken` 저장하고 토스트 메시지를 띄우고 화면 종료 (`finish()`)**   
✅ **`SettingsActivity`의 `onResume()`에서 `loadUserInfo()` 추가하여 유저 정보 최신화**

---

## Settings - 비밀번호 변경

### 1️⃣ 비밀번호 변경 UI

**🔹 `activity_settings_password_change.xml`**

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/white"
    android:paddingVertical="24dp"
    android:paddingHorizontal="24dp"
    tools:context=".settings.activity.PasswordChangeActivity">

    <!-- 뒤로가기 버튼 -->
    <ImageButton
        android:id="@+id/backButton"
        android:layout_width="24dp"
        android:layout_height="24dp"
        android:scaleType="fitCenter"
        android:src="@drawable/icon_arrow_back_48dp"
        android:contentDescription="@string/btn_back"
        android:background="@color/white"
        android:onClick="onBackButtonClick"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>

    <!-- 현재 비밀번호 라벨 -->
    <TextView
        android:id="@+id/passwordNowLabel"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/password_now"
        app:layout_constraintTop_toBottomOf="@id/backButton"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="16dp"/>

    <!-- 현재 비밀번호 입력 -->
    <EditText
        android:id="@+id/passwordNowInput"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:hint="@string/password_input_now"
        android:importantForAutofill="no"
        android:inputType="textPassword"
        app:layout_constraintTop_toBottomOf="@id/passwordNowLabel"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="8dp"/>

    <!-- 새 비밀번호 라벨 -->
    <TextView
        android:id="@+id/passwordNewLabel"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/password_new"
        app:layout_constraintTop_toBottomOf="@id/passwordNowInput"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="16dp"/>

    <!-- 새 비밀번호 입력 -->
    <EditText
        android:id="@+id/passwordNewInput"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:hint="@string/password_input_new"
        android:importantForAutofill="no"
        android:inputType="textPassword"
        app:layout_constraintTop_toBottomOf="@id/passwordNewLabel"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="8dp"/>

    <!-- 새 비밀번호 확인 라벨 -->
    <TextView
        android:id="@+id/passwordCheckLabel"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/password_check"
        app:layout_constraintTop_toBottomOf="@id/passwordNewInput"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="16dp"/>

    <!-- 새 비밀번호 확인 입력 -->
    <EditText
        android:id="@+id/passwordCheckInput"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:hint="@string/password_input_new"
        android:importantForAutofill="no"
        android:inputType="textPassword"
        app:layout_constraintTop_toBottomOf="@id/passwordCheckLabel"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="8dp"/>

    <!-- 변경 버튼 -->
    <Button
        android:id="@+id/changeButton"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@string/btn_change"
        android:onClick="onChangeButtonClick"
        app:layout_constraintTop_toBottomOf="@id/passwordCheckInput"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="16dp"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### 🔹 UI 미리보기
아래 이미지는 비밀번호 변경 화면의 디자인을 보여줍니다.  
<img src="./img/page_settings_password_change.jpg" width="200" height="400" /> <br />

---

### 2️⃣ DTO (Data Transfer Object) 설계

#### **`PasswordChangeRequest.java`**

```java
public class PasswordChangeRequest {
    @SerializedName("passwordNow")
    private final String passwordNow; // ✅ 현재 비밀번호

    @SerializedName("passwordNew")
    private final String passwordNew; // ✅ 새 비밀번호

    @SerializedName("passwordCheck")
    private final String passwordCheck; // ✅ 새 비밀번호 확인

    public PasswordChangeRequest(String passwordNow, String passwordNew, String passwordCheck) {
        this.passwordNow = passwordNow;
        this.passwordNew = passwordNew;
        this.passwordCheck = passwordCheck;
    }

    public String getPasswordNow() {
        return passwordNow;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }

    @NonNull
    @Override
    public String toString() {
        return "PasswordChangeRequest{" +
                "passwordNow='" + passwordNow + '\'' +
                ", passwordNew='" + passwordNew + '\'' +
                ", passwordCheck='" + passwordCheck + '\'' +
                '}';
    }
}
```

### 3️⃣ Activity 설계

```java
/**
 * 📌 비밀번호 변경(Password Change) 화면
 *
 * - 사용자가 기존 비밀번호를 변경할 수 있는 액티비티
 * - 현재 비밀번호와 새 비밀번호, 새 비밀번호 확인 입력 가능
 * - 비밀번호 변경 시 유효성 검사 적용 가능
 */
public class PasswordChangeActivity extends AppCompatActivity {
    EditText passwordNowInput, passwordNewInput, passwordCheckInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_password_change);

        passwordNowInput = findViewById(R.id.passwordNowInput);
        passwordNewInput = findViewById(R.id.passwordNewInput);
        passwordCheckInput = findViewById(R.id.passwordCheckInput);
    }

    /**
     * 뒤로가기 버튼 클릭 이벤트 핸들러
     * @param view 클릭된 뷰
     */
    public void onBackButtonClick(View view) {
        finish();
    }

    /**
     * 변경 버튼 클릭 이벤트 핸들러
     * @param view 클릭된 뷰
     */
    public void onChangeButtonClick(View view) {
        String pwNow = passwordNowInput.getText().toString().trim();
        String pwNew = passwordNewInput.getText().toString().trim();
        String pwCheck = passwordCheckInput.getText().toString().trim();

        if (pwNow.isEmpty() || pwNew.isEmpty() || pwCheck.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (pwNew.length() < 8 || pwNew.length() > 20) {
            Toast.makeText(this, "비밀번호는 8자 이상 20자 이하이어야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (pwNew.equals(pwNow)) {
            Toast.makeText(this, "기존 비밀번호와 새 비밀번호가 일치합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pwNew.equals(pwCheck)) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // API 요청 객체 생성
        PasswordChangeRequest request = new PasswordChangeRequest(pwNow, pwNew, pwCheck);

        // API 요청 실행
        AuthApiService apiService = RetrofitClient.getClient().create(AuthApiService.class);
        Call<Void> call = apiService.changePassword(request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PasswordChangeActivity.this, "비밀번호 변경 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PasswordChangeActivity.this, "비밀번호 변경 실패: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(PasswordChangeActivity.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

---

## 구글 로그인 연동

안드로이드 앱에 **구글 로그인**을 연동하기 위한 단계별 설정 방법입니다.

### 1️⃣ Google Cloud Console 설정

1. [Google Cloud Console](https://console.cloud.google.com/) 접속 → **새 프로젝트 생성**
2. **OAuth 동의 화면 설정**
   - 사용자 유형 선택 (내부 또는 외부)
   - 앱 이름, 이메일, 권한 범위 설정
3. **OAuth 2.0 클라이언트 ID 생성**
   - **애플리케이션 유형**: Android 선택
   - **패키지명 입력** (`AndroidManifest.xml`의 `package="..."` 값과 동일해야 함 or `build.gradle` 의 `applicationId`)
   - **SHA1 키 입력** (`keytool -exportcert`로 SHA1 키 생성 후 입력)
4. **클라이언트 ID 및 비밀키 저장**
   - 생성된 `client_id.json` 파일 다운로드하여 사용

---

### 2️⃣ Gradle 의존성 추가 (Version Catalog - `libs.versions.toml` 기반)

**🔹 `libs.versions.toml` (버전 관리 파일)**
```toml
[versions]
google-auth = "20.5.0"

[libraries]
google-auth = { module = "com.google.android.gms:play-services-auth", version.ref = "google-auth" }
```

**🔹 `build.gradle.kts` (모듈 수준)**
```kotlin
defaultConfig {
   val googleClientId: String = project.findProperty("GOOGLE_CLIENT_ID") as String? ?: "YOUR_DEFAULT_CLIENT_ID"
   buildConfigField("String", "GOOGLE_CLIENT_ID", "\"$googleClientId\"")
   resValue("string", "google_client_id", googleClientId)
   
   val googleWebClientId: String = project.findProperty("GOOGLE_WEB_CLIENT_ID") as String? ?: "YOUR_DEFAULT_WEB_CLIENT_ID"
   buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
}

dependencies {
    implementation(libs.google.auth)
}
```

> ✅ 위 코드는 `gradle.properties` 또는 `local.properties`에 설정된 값을 앱에서 상수로 사용 가능하게 설정합니다.

---

### 3️⃣ AndroidManifest.xml 설정
```xml
<uses-permission android:name="android.permission.INTERNET" />

<application>
    <meta-data
        android:name="com.google.android.gms.auth.api.signin.CLIENT_ID"
        android:value="@string/default_web_client_id" />
</application>
```

> `@string/default_web_client_id`는 `google-services.json`을 통해 자동 생성되는 값입니다. 또는 직접 `BuildConfig.GOOGLE_CLIENT_ID`로 설정 가능.

---

### 4️⃣ 로그인 구현 (LoginActivity)
```java
public class LoginActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> signInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ✅ 로그인 옵션 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // 사용자 이메일 요청
                .requestProfile() // 사용자 프로필 정보 요청 (이름, 사진 등)
                .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID) // ✅ 서버 인증을 위한 idToken 요청
                .build();

        // ✅ Google 로그인 클라이언트 초기화
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // ✅ 로그인 결과 처리 콜백 등록
        signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    handleSignInResult(task);
                }
            }
        );

        // ✅ 버튼 클릭 시 로그인 실행
        Button googleLoginBtn = findViewById(R.id.btn_google_login);
        googleLoginBtn.setOnClickListener(v -> signIn());
    }

    // ✅ 로그인 인텐트 실행
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    // ✅ 로그인 성공 여부 처리
    // 로그인 성공 여부 처리
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String imageUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : null;

            AuthApiService apiService = RetrofitClient.getClient().create(AuthApiService.class);
            Call<LoginResponse> call = apiService.loginForGoogle(
                    new GoogleLoginRequest(
                            account.getEmail(),
                            account.getDisplayName(),
                            imageUrl,
                            account.getIdToken()
                    )
            );

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

        } catch (ApiException e) {
            Log.e("GOOGLE_LOGIN", "로그인 실패", e);
        }
    }
    
    private void loginSuccess(LoginResponse loginResponse) {
        TokenManager.saveTokens(loginResponse.getAccessToken(), loginResponse.getRefreshToken());

        // ✅ UserSession 초기화 → JWT 해석하여 유저 정보 저장
        UserSession.setAccessToken(loginResponse.getAccessToken());

        Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(LoginActivity.this, ScheduleActivity.class));
        finish();
    }
}
```

#### 🔍 주요 메소드 설명
| 메소드 | 설명 |
|--------|------|
| `onCreate()` | 로그인 구성 요소 초기화 및 버튼 클릭 리스너 등록 |
| `signIn()` | 로그인 인텐트를 실행하여 Google 계정 선택 화면 호출 |
| `signInLauncher` | 로그인 결과를 수신하는 콜백 등록 (Activity Result API 사용) |
| `handleSignInResult()` | 로그인 성공 시 사용자 정보(email, nickname, imageUrl, idToken) 추출 및 서버 연동 처리 |

---

### 5️⃣ 서버 전송용 DTO - `GoogleLoginRequest.java`
```java
public class GoogleLoginRequest {
    @SerializedName("email")
    private final String email;

    @SerializedName("nickname")
    private final String nickname;

    @SerializedName("imageUrl")
    private final String imageUrl;

    @SerializedName("idToken")
    private final String idToken;

    public GoogleLoginRequest(String email, String nickname, String imageUrl, String idToken) {
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.idToken = idToken;
    }

    public String getEmail() { return email; }
    public String getNickname() { return nickname; }
    public String getImageUrl() { return imageUrl; }
    public String getIdToken() { return idToken; }

    @NonNull
    @Override
    public String toString() {
        return "GoogleLoginRequest{" +
                "email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", idToken='" + idToken + '\'' +
                '}';
    }
}
```

---

### ✅ 추가 팁
- **서버 인증 토큰 요청**: `.requestIdToken("서버 클라이언트 ID")` 사용 (서버 인증 필요 시)
- **권한 범위 설명**:
  - `email`: 이메일 주소
  - `profile`: 프로필 정보 (닉네임, 이미지)
  - `openid`: ID 토큰을 발급받기 위한 필수 범위 (서버 인증 필요 시)

---

### 6️⃣ `AuthApiService` 추가
```java
public interface AuthApiService {
    @POST("api/auth/google/login")
    Call<LoginResponse> loginForGoogle(@Body GoogleLoginRequest request);
}
```

---








