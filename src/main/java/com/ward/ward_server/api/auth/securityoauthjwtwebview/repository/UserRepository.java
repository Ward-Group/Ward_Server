package com.ward.ward_server.api.auth.securityoauthjwtwebview.repository;

import com.ward.ward_server.api.auth.securityoauthjwtwebview.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
}
