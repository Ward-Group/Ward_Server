package com.ward.ward_server.api.entry.service;

import com.ward.ward_server.api.entry.controller.EntryRequestDTO;
import com.ward.ward_server.api.entry.domain.EntryRecord;
import com.ward.ward_server.api.entry.repository.EntryRepository;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.api.webcrawling.ItemRepository;
import com.ward.ward_server.api.webcrawling.entity.Item;
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

    @Transactional
    public Long createEntry(EntryRequestDTO entryRequestDTO) {
        // 요청된 사용자 ID와 아이템 ID로 이미 응모 기록이 있는지 확인
        boolean isEntryExist = entryRepository.existsByUserIdAndItemId(entryRequestDTO.getUserId(), entryRequestDTO.getItemId());
        if (isEntryExist) {
            throw new ApiException(DUPLICATE_ENTRY); // 이미 응모한 경우 에러 발생
        }

        //엔티티 조회
        User user = userRepository.findById(entryRequestDTO.getUserId())
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Item item = itemRepository.findById(entryRequestDTO.getItemId())
                .orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));

        //응모 내역 생성
        EntryRecord entryRecord = new EntryRecord(user,item);

        //응모 저장
        entryRepository.save(entryRecord);

        return entryRecord.getEntryId();
    }

//    public EntryRecord getEntryById(Long entryId) {
//        // entryId를 사용하여 특정 응모 내역을 조회하고 반환
//        return entryRepository.findById(entryId).orElse(null);
//    }
//
    public List<EntryRecord> getUsersEntryRecord(Long userId) {
        // userId를 사용하여 특정 사용자의 전체 응모 내역 조회하고 반환

        // 사용자 엔티티 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("[Slf4j]User not found with ID: {}", userId);
                    return new ApiException(USER_NOT_FOUND);
                });

        // 사용자의 전체 응모 내역 조회
        List<EntryRecord> allByUserId = entryRepository.findAllByUser(user);

        return allByUserId;
    }
//
//    @Transactional
//    public void deleteEntry(Long entryId) {
//        // entryId를 사용하여 특정 응모 내역을 삭제
//        entryRepository.deleteById(entryId);
//    }
}
