package com.sometodo.app.auth.repository;

import com.sometodo.app.auth.entity.Account;
import com.sometodo.app.auth.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByNickname(String nickname);
    Optional<Profile> findByAccount(Account account);
}
