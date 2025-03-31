package com.sometodo.app.auth.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {
    private Long id; // 프로필 ID
    private Long accountId; // 연결된 Account ID
    private String nickname; // 닉네임
    private String imageUrl; // 프로필 이미지 URL
    private String statusMessage; // 상태 메시지
    private LocalDateTime updatedAt; // 마지막 수정일
}
