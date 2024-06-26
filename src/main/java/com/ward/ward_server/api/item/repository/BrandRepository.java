package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.repository.query.BrandQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long>, BrandQueryRepository {

    List<Brand> findTop10ByOrderByViewCountDesc();

    @Query("SELECT COUNT(b) > 0 " +
            "FROM Brand b " +
            "WHERE (:koreanName IS NULL OR b.koreanName = :koreanName) " +
            "AND (:englishName IS NULL OR b.englishName = :englishName)")
    boolean existsByKoreanNameOrEnglishName(@Param("koreanName") String koreanName, @Param("englishName") String englishName);
}
