package com.ward.ward_server.api.releaseInfo.repository;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.repository.query.ReleaseInfoQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ReleaseInfoRepository extends JpaRepository<ReleaseInfo, Long>, ReleaseInfoQueryRepository {

    Page<ReleaseInfo> findByItemAndDueDateAfter(Item item, LocalDateTime now, Pageable pageable);

    Page<ReleaseInfo> findByItemAndDueDateBefore(Item item, LocalDateTime now, Pageable pageable);

    boolean existsByItemIdAndDrawPlatform(long itemId, DrawPlatform drawPlatform);
    @Query("SELECT DISTINCT r.item.id " +
            "FROM ReleaseInfo r " +
            "WHERE r.id In :releaseInfoIds ")
    List<Long> findItemIdsIn(@Param("releaseInfoIds") Set<Long> releaseInfoIds);
}
