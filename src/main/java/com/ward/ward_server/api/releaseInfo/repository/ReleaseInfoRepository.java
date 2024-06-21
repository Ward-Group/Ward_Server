package com.ward.ward_server.api.releaseInfo.repository;

import com.ward.ward_server.api.item.entity.enumtype.Status;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReleaseInfoRepository extends JpaRepository<ReleaseInfo, Long> {
    List<ReleaseInfo> findAllByItemId(long itemId);

    Optional<ReleaseInfo> findByItemIdAndDrawPlatform(long itemId, DrawPlatform drawPlatform);

    Page<ReleaseInfo> findByItemIdAndStatus(Long itemId, Status status, Pageable pageable);

    boolean existsByItemIdAndDrawPlatform(long itemId, DrawPlatform drawPlatform);

    void deleteByItemIdAndDrawPlatform(long itemId, DrawPlatform drawPlatform);
}
