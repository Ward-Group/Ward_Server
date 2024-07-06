package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.ItemViewCount;
import com.ward.ward_server.api.item.entity.enums.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemViewCountRepository extends JpaRepository<ItemViewCount, Long> {
    Optional<ItemViewCount> findByItemAndCategory(Item item, Category category);

    @Query("SELECT ivc FROM ItemViewCount ivc " +
            "JOIN FETCH ivc.item i " +
            "JOIN FETCH i.brand b " +
            "WHERE ivc.category = :category " +
            "ORDER BY ivc.viewCount DESC")
    List<ItemViewCount> findTopItemsByCategoryWithFetchJoin(@Param("category") Category category, Pageable pageable);

    @Query("SELECT ivc FROM ItemViewCount ivc " +
            "JOIN FETCH ivc.item i " +
            "JOIN FETCH i.brand b " +
            "ORDER BY ivc.viewCount DESC")
    List<ItemViewCount> findTopItemsForAllCategoriesWithFetchJoin(Pageable pageable);
}
