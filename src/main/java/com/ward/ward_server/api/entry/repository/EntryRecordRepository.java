package com.ward.ward_server.api.entry.repository;

import com.ward.ward_server.api.entry.entity.EntryRecord;
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
}
