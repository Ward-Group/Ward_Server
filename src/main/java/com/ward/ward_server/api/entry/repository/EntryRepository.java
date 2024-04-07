package com.ward.ward_server.api.entry.repository;

import com.ward.ward_server.api.entry.domain.EntryRecord;
import com.ward.ward_server.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<EntryRecord, Long> {
    List<EntryRecord> findAllByUserId(Long userId);

    List<EntryRecord> findAllByUser(User user);

    // 사용자 ID와 아이템 ID를 기반으로 해당 응모 기록이 존재하는지 확인하는 메서드
    boolean existsByUserIdAndItemId(Long userId, Long itemId);
}
