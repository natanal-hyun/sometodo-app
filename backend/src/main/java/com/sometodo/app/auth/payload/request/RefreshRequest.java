package com.sometodo.app.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshRequest {

    @NotBlank(message = "토큰을 입력해주세요.")
    private String refreshToken;

}
