package com.ward.ward_server.api.user.repository;

import com.ward.ward_server.api.user.entity.SocialLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {
    boolean existsByEmail(String email);
    Optional<SocialLogin> findByProviderAndProviderIdAndEmail(String provider, String providerId, String email);
}
