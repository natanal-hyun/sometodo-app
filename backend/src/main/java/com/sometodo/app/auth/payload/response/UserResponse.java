package com.sometodo.app.auth.payload.response;

import com.sometodo.app.auth.enums.UserRole;
import lombok.*;

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
