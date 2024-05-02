package com.ward.ward_server.api.releaseInfo.repository;

import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrawPlatformRepository extends JpaRepository<DrawPlatform, Long> {
    boolean existsByName(String name);
    Optional<DrawPlatform> findByName(String name);
}
