package com.ward.ward_server.api.entry.repository;

import com.ward.ward_server.api.entry.domain.EntryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<EntryRecord, Long> {
    List<EntryRecord> findAllByUserId(Long userId);

    @Query("SELECT er FROM EntryRecord er JOIN FETCH er.user JOIN FETCH er.item WHERE er.user.id = :userId")
    List<EntryRecord> findAllByUserIdWithFetch(@Param("userId") Long userId);

    // 사용자 ID와 아이템 ID를 기반으로 해당 응모 기록이 존재하는지 확인하는 메서드
    boolean existsByUserIdAndItemId(Long userId, Long itemId);

    void deleteByUserIdAndItemId(Long userId, Long itemId);
}
