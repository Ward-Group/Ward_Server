package com.ward.ward_server.api.entry.repository;

import com.ward.ward_server.api.entry.entity.EntryRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRecordRepository extends JpaRepository<EntryRecord, Long> {

    boolean existsByUserIdAndReleaseInfoId(long userId, long releaseInfoId);
    void deleteByUserIdAndReleaseInfoId(long userId, long releaseInfoId);
    List<EntryRecord> findAllByUserIdAndReleaseInfoIdIn(long userId, List<Long> releaseInfoIds);
    Page<EntryRecord> findAllByUserId(long userId, Pageable pageable);
}
