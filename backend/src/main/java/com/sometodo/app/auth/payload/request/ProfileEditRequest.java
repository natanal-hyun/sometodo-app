package com.sometodo.app.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @Size(max = 50, message = "상태말은 50자 이하로 입력해주세요.")
    private String statusMessage;
}
