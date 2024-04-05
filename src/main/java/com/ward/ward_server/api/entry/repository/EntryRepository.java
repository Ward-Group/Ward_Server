package com.ward.ward_server.api.entry.repository;

import com.ward.ward_server.api.entry.domain.EntryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<EntryRecord, Long> {
    List<EntryRecord> findAllByUserId(Long userId);
}
