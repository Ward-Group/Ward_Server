package com.ward.ward_server.api.releaseInfo.repository;

import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.repository.query.ReleaseInfoQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReleaseInfoRepository extends JpaRepository<ReleaseInfo, Long>, ReleaseInfoQueryRepository {
    Optional<ReleaseInfo> findById(long id);

    Optional<Long> findIdByItemIdAndDrawPlatformId(long itemId, long platformId);

    boolean existsByItemIdAndDrawPlatform(long itemId, DrawPlatform drawPlatform);
}
