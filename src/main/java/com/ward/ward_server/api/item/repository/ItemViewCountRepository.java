package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.ItemViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemViewCountRepository extends JpaRepository<ItemViewCount, Long> {

    @Query("SELECT ivc FROM ItemViewCount ivc " +
            "WHERE ivc.calculatedAt BETWEEN :startTime AND :endTime")
    List<ItemViewCount> findViewCountsBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
