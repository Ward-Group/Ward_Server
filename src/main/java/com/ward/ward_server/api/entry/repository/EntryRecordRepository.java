package com.ward.ward_server.api.entry.repository;

import com.ward.ward_server.api.entry.dto.EntryDetailResponse;
import com.ward.ward_server.api.entry.entity.EntryRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntryRecordRepository extends JpaRepository<EntryRecord, Long> {

    boolean existsByUserIdAndReleaseInfoId(long userId, long releaseInfoId);

    void deleteByUserIdAndReleaseInfoId(long userId, long releaseInfoId);

    Optional<EntryRecord> findByUserIdAndReleaseInfoId(long userId, long releaseInfoId);

    @Query("SELECT COUNT(e) FROM EntryRecord e WHERE e.user.id = :userId")
    long countByUserId(@Param("userId") long userId);

    @Query("SELECT COUNT(e) FROM EntryRecord e WHERE e.user.id = :userId AND e.releaseInfo.dueDate > CURRENT_TIMESTAMP")
    long countInProgressByUserId(@Param("userId") long userId);

    @Query("SELECT COUNT(e) FROM EntryRecord e WHERE e.user.id = :userId AND e.releaseInfo.presentationDate <= CURRENT_TIMESTAMP")
    long countAnnouncementByUserId(@Param("userId") long userId);

    @Query("SELECT COUNT(e) FROM EntryRecord e WHERE e.user.id = :userId AND e.releaseInfo.dueDate <= CURRENT_TIMESTAMP AND e.releaseInfo.presentationDate > CURRENT_TIMESTAMP")
    long countClosedByUserId(@Param("userId") long userId);

    @Query("SELECT new com.ward.ward_server.api.entry.dto.EntryDetailResponse(e.releaseInfo.item.mainImage, e.releaseInfo.drawPlatform.englishName, e.releaseInfo.item.koreanName, e.releaseInfo.item.code, e.releaseInfo.id) " +
            "FROM EntryRecord e " +
            "WHERE e.user.id = :userId " +
            "ORDER BY e.id DESC")
    Page<EntryDetailResponse> findAllByUserId(@Param("userId") long userId, Pageable pageable);

    @Query("SELECT new com.ward.ward_server.api.entry.dto.EntryDetailResponse(e.releaseInfo.item.mainImage, e.releaseInfo.drawPlatform.englishName, e.releaseInfo.item.koreanName, e.releaseInfo.item.code, e.releaseInfo.id) " +
            "FROM EntryRecord e " +
            "WHERE e.user.id = :userId AND e.releaseInfo.dueDate > CURRENT_TIMESTAMP " +
            "ORDER BY e.id DESC")
    Page<EntryDetailResponse> findInProgressByUserId(@Param("userId") long userId, Pageable pageable);

    @Query("SELECT new com.ward.ward_server.api.entry.dto.EntryDetailResponse(e.releaseInfo.item.mainImage, e.releaseInfo.drawPlatform.englishName, e.releaseInfo.item.koreanName, e.releaseInfo.item.code, e.releaseInfo.id) " +
            "FROM EntryRecord e " +
            "WHERE e.user.id = :userId AND e.releaseInfo.presentationDate <= CURRENT_TIMESTAMP " +
            "ORDER BY e.id DESC")
    Page<EntryDetailResponse> findAnnouncementByUserId(@Param("userId") long userId, Pageable pageable);

    @Query("SELECT new com.ward.ward_server.api.entry.dto.EntryDetailResponse(e.releaseInfo.item.mainImage, e.releaseInfo.drawPlatform.englishName, e.releaseInfo.item.koreanName, e.releaseInfo.item.code, e.releaseInfo.id) " +
            "FROM EntryRecord e " +
            "WHERE e.user.id = :userId AND e.releaseInfo.dueDate <= CURRENT_TIMESTAMP AND e.releaseInfo.presentationDate > CURRENT_TIMESTAMP " +
            "ORDER BY e.id DESC")
    Page<EntryDetailResponse> findClosedByUserId(@Param("userId") long userId, Pageable pageable);

}
