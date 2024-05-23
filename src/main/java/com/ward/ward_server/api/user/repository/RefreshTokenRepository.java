package com.ward.ward_server.api.user.repository;

import com.ward.ward_server.api.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenAndInvalidFalse(String token);
    Optional<RefreshToken> findByToken(String token);
}
