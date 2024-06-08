package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long>, BrandQueryRepository {
    @Query("SELECT COUNT(b) > 0 FROM Brand b WHERE b.englishName = :name OR b.koreanName = :name")
    boolean existsByName(@Param("name") String name);

    boolean existsByKoreanNameOrEnglishName(String koreanName, String englishName);

    @Query("SELECT b FROM Brand b WHERE b.englishName = :name OR b.koreanName = :name")
    Optional<Brand> findByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM Brand b WHERE b.englishName = :name OR b.koreanName = :name")
    void deleteByName(@Param("name") String name);
}
