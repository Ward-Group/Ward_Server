package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemQueryRepository{
    Optional<Item> findByCodeAndBrandIdAndDeletedAtIsNull(String code, long brandId);

    Page<Item> findAllByDeletedAtIsNull(Pageable pageable);

    boolean existsByCodeAndBrandId(String code, long brandId);

    @Query(value = "SELECT i.* " +
            "FROM item i LEFT JOIN wish_item wi ON i.id = wi.item_id " +
            "WHERE i.brand_id = :brandId " +
            "GROUP BY i.id " +
            "ORDER BY i.view_count + COUNT(wi.id) DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Item> findBrandItemListTop3(@Param("brandId") long brandId);
}
