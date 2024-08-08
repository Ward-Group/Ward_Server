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

public interface ReleaseInfoRepository extends JpaRepository<ReleaseInfo, Long>, ReleaseInfoQueryRepository {

    Page<ReleaseInfo> findByItemAndDueDateAfter(Item item, LocalDateTime now, Pageable pageable);

    Page<ReleaseInfo> findByItemAndDueDateBefore(Item item, LocalDateTime now, Pageable pageable);

    boolean existsByItemIdAndDrawPlatform(long itemId, DrawPlatform drawPlatform);

    @Query("SELECT COUNT(r) FROM ReleaseInfo r WHERE r.item.id = :itemId")
    int countByItemId(@Param("itemId") Long itemId);

    @Query("SELECT ri FROM ReleaseInfo ri " +
            "JOIN ri.item i " +
            "JOIN ri.drawPlatform dp " +
            "JOIN i.brand b " +
            "WHERE LOWER(i.koreanName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(i.englishName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(i.code) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.koreanName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.englishName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(dp.koreanName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(dp.englishName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ReleaseInfo> searchReleaseInfos(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT r.item.id, r.item.mainImage, MIN(r.dueDate) as dueDate " +
            "FROM ReleaseInfo r " +
            "WHERE r.dueDate > :now " +
            "GROUP BY r.item.id, r.item.mainImage " +
            "ORDER BY dueDate ASC")
    List<Object[]> findExpiringItems(@Param("now") LocalDateTime now, Pageable pageable);
}
