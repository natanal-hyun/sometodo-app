package com.sometodo.app.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
