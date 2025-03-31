package com.example.todolist.settings.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.todolist.R;
import com.example.todolist.auth.network.RetrofitClient;
import com.example.todolist.auth.token.TokenManager;
import com.example.todolist.auth.token.UserSession;
import com.example.todolist.settings.dto.ProfileEditRequest;
import com.example.todolist.settings.dto.ProfileEditResponse;
import com.example.todolist.settings.network.SettingsApiService;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 📌 프로필 수정(Profile Edit) 화면
 *
 * - 사용자가 닉네임, 프로필 이미지, 상태 메시지를 변경할 수 있는 액티비티
 * - 변경된 정보를 저장하면 서버로 업데이트 요청을 보냄
 * - 프로필 이미지 변경을 위해 `Glide` 라이브러리 사용 가능
 */
public class ProfileEditActivity extends AppCompatActivity {
    ImageView profileImage;
    EditText imageUrlInput, nicknameInput, statusMessageInput;
    TextView emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile_edit);

        profileImage = findViewById(R.id.profileImage);
        imageUrlInput = findViewById(R.id.imageUrlInput);
        nicknameInput = findViewById(R.id.nicknameInput);
        emailText = findViewById(R.id.emailText);
        statusMessageInput = findViewById(R.id.statusMessageInput);

        // 유저 정보 설정
        loadUserInfo();

        imageUrlInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 텍스트 변경 전에 실행 (주로 사용되지 않음)
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력된 텍스트가 변경될 때 실행 (Glide로 이미지 업데이트)
                loadProfileImage(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 텍스트 변경이 완료된 후 실행 (보통 사용하지 않음)
            }
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
            String statusMessage = userClaims.optString("statusMessage", "");

            nicknameInput.setText(nickname);
            emailText.setText(email);
            imageUrlInput.setText(imageUrl);
            statusMessageInput.setText(statusMessage);

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
                .load(imageUrl)
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
        // 입력된 닉네임과 이미지 URL 가져오기
        String newNickname = nicknameInput.getText().toString().trim();
        String newImageUrl = imageUrlInput.getText().toString().trim();
        String newStatusMessage = statusMessageInput.getText().toString().trim();

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

        if (newStatusMessage.length() > 50) {
            Toast.makeText(this, "상태 메세지는 50자 이하이어야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // API 요청 객체 생성
        ProfileEditRequest request = new ProfileEditRequest(newNickname, newImageUrl, newStatusMessage);

        // API 요청 실행
        SettingsApiService apiService = RetrofitClient.getClient().create(SettingsApiService.class);
        Call<ProfileEditResponse> call = apiService.updateProfile(request);

        call.enqueue(new Callback<ProfileEditResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProfileEditResponse> call, @NonNull Response<ProfileEditResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String newAccessToken = response.body().getAccessToken();
                    if (newAccessToken != null && !newAccessToken.isEmpty()) {
                        TokenManager.setAccessToken(newAccessToken); // ✅ 새로운 Access Token 저장
                        UserSession.setAccessToken(newAccessToken);
                    }

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
}
