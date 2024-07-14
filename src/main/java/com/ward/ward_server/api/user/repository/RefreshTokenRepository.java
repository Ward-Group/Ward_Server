package com.ward.ward_server.api.user.repository;

import com.ward.ward_server.api.user.entity.RefreshToken;
import com.ward.ward_server.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}
