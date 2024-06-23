package com.ward.ward_server.api.releaseInfo.repository;

import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ward.ward_server.api.releaseInfo.repository.query.ReleaseInfoQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReleaseInfoRepository extends JpaRepository<ReleaseInfo, Long>, ReleaseInfoQueryRepository {
    Optional<ReleaseInfo> findById(long id);

    Optional<Long> findIdByItemIdAndDrawPlatformId(long itemId, long platformId);

    Page<ReleaseInfo> findByItemIdAndDueDateAfter(Long itemId, LocalDateTime now, Pageable pageable);

    Page<ReleaseInfo> findByItemIdAndDueDateBefore(Long itemId, LocalDateTime now, Pageable pageable);


    boolean existsByItemIdAndDrawPlatform(long itemId, DrawPlatform drawPlatform);
}
