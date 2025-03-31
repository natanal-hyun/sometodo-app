package com.sometodo.app.auth.payload.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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