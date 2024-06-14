package com.ward.ward_server.api.releaseInfo.repository;

import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DrawPlatformRepository extends JpaRepository<DrawPlatform, Long> {
    @Query("SELECT COUNT(d) > 0 " +
            "FROM DrawPlatform d " +
            "WHERE d.englishName = :name OR d.koreanName = :name")
    boolean existsByName(@Param("name") String name);

    @Query("SELECT COUNT(d) > 0 " +
            "FROM DrawPlatform d " +
            "WHERE (:koreanName IS NULL OR d.koreanName = :koreanName) " +
            "AND (:englishName IS NULL OR d.englishName = :englishName)")
    boolean existsByKoreanNameOrEnglishName(@Param("koreanName") String koreanName, @Param("englishName") String englishName);

    @Query("SELECT d " +
            "FROM DrawPlatform d " +
            "WHERE d.englishName = :name OR d.koreanName = :name")
    Optional<DrawPlatform> findByName(@Param("name") String name);
}
