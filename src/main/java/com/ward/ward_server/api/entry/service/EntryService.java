package com.ward.ward_server.api.entry.service;

import com.ward.ward_server.api.entry.controller.EntryRequestDTO;
import com.ward.ward_server.api.entry.domain.EntryRecord;
import com.ward.ward_server.api.entry.repository.EntryRepository;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class EntryService {
    private final EntryRepository entryRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    // 응모 내역 1건 존재 여부 확인
    public boolean isEntryExist(Long userId, Long itemId) {
        return entryRepository.existsByUserIdAndItemId(userId, itemId);
    }

    // 응모 내역 추가
    @Transactional
    public Long createEntry(EntryRequestDTO entryRequestDTO) {

        long userId = entryRequestDTO.getUserId();
        long itemId = entryRequestDTO.getItemId();
        // 요청된 사용자 ID와 아이템 ID로 이미 응모 기록이 있는지 확인
        boolean isEntryExist = isEntryExist(userId, itemId);
        if (isEntryExist) {
            throw new ApiException(DUPLICATE_ENTRY); // 이미 응모한 경우 에러 발생
        }

        //엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));

        //응모 내역 생성
        EntryRecord entryRecord = new EntryRecord(user,item);

        //응모 저장
        entryRepository.save(entryRecord);

        return entryRecord.getEntryId();
    }

    // 응모 내역 단건 조회
    public EntryRecord getEntryById(Long entryId) {

        // entryId를 사용하여 특정 응모 내역을 조회하고 반환
        EntryRecord entryRecord = entryRepository.findById(entryId).orElse(null);

        return entryRecord;
    }

    // 응모 내역 전체 조회 - user 1명에 대해
    public List<EntryRecord> getUsersAllEntryRecord(Long userId) {
        // userId를 사용하여 특정 사용자의 전체 응모 내역 조회하고 반환

        // 사용자 엔티티 조회
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> {
//                    log.error("[Slf4j]User not found with ID: {}", userId);
//                    return new ApiException(USER_NOT_FOUND);
//                });

        // 사용자의 전체 응모 내역 조회
//        List<EntryRecord> allByUserId = entryRepository.findAllByUser(user);

        // 사용자의 전체 응모 내역 조회 (패치 조인 사용)
        List<EntryRecord> allByUserId = entryRepository.findAllByUserIdWithFetch(userId);

        return allByUserId;
    }

    @Transactional
    public void deleteEntry(EntryRequestDTO entryRequestDTO) {

        long userId = entryRequestDTO.getUserId();
        long itemId = entryRequestDTO.getItemId();

        boolean isEntryExist = isEntryExist(userId, itemId);
        if (!isEntryExist) {
            throw new ApiException(NO_ENTRY_RECORD_FOUND); // 응모 내역이 존재하지 않음
        }

        // userId, entryId를 사용하여 특정 응모 내역을 삭제
        entryRepository.deleteByUserIdAndItemId(userId,itemId);
    }
}
