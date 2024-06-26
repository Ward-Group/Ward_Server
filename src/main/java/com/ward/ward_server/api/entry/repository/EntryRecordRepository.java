package com.ward.ward_server.api.entry.repository;

import com.ward.ward_server.api.entry.entity.EntryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EntryRecordRepository extends JpaRepository<EntryRecord, Long> {

    boolean existsByUserIdAndReleaseInfoId(long userId, long releaseInfoId);

    void deleteByUserIdAndReleaseInfoId(long userId, long releaseInfoId);

    Optional<EntryRecord> findByUserIdAndReleaseInfoId(long userId, long releaseInfoId);
}
