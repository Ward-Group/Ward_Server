package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.ItemViewCount;
import com.ward.ward_server.api.item.entity.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemViewCountRepository extends JpaRepository<ItemViewCount, Long> {
    Optional<ItemViewCount> findByItemAndCategory(Item item, Category category);

    @Query("SELECT ivc FROM ItemViewCount ivc " +
            "WHERE ivc.calculatedAt BETWEEN :startTime AND :endTime")
    List<ItemViewCount> findViewCountsBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
