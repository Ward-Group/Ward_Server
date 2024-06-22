package com.ward.ward_server.api.releaseInfo.repository;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.repository.query.ReleaseInfoQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface ReleaseInfoRepository extends JpaRepository<ReleaseInfo, Long>, ReleaseInfoQueryRepository {
    List<ReleaseInfo> findAllByItemId(long itemId);
    Optional<ReleaseInfo> findByItemIdAndDrawPlatform(long itemId, DrawPlatform drawPlatform);
    boolean existsByItemIdAndDrawPlatform(long itemId, DrawPlatform drawPlatform);
}
