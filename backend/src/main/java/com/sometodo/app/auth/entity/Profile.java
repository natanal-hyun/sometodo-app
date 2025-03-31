package com.sometodo.app.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(length = 50)
    private String statusMessage; // 상태 메시지 (한 줄 소개)

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now(); // 마지막 수정일
}
