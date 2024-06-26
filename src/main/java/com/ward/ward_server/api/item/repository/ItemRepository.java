package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.repository.query.ItemQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemQueryRepository {
    Optional<Item> findByCodeAndBrandIdAndDeletedAtIsNull(String code, long brandId);
    @Query("SELECT i.id " +
            "FROM Item i " +
            "WHERE i.code = :code " +
            "AND i.brand.id = :brandId " +
            "AND i.deletedAt IS NULL")
    Optional<Long> findIdByCodeAndBrandId(String code, long brandId);

    boolean existsByCodeAndBrandId(String code, long brandId);

    Optional<Item> findByIdAndDeletedAtIsNull(Long id);

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.koreanName LIKE %:keyword% " +
            "OR i.englishName LIKE %:keyword% " +
            "OR i.brand.koreanName LIKE %:keyword% " +
            "OR i.brand.englishName LIKE %:keyword% " +
            "OR i.code LIKE %:keyword% " +
            "ORDER BY i.viewCount DESC")
    Page<Item> searchItems(@Param("keyword") String keyword, Pageable pageable);
}
