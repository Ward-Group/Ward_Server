package com.ward.ward_server.api.entry.service;

import com.ward.ward_server.api.entry.controller.EntryRequestDTO;
import com.ward.ward_server.api.entry.domain.EntryRecord;
import com.ward.ward_server.api.entry.repository.EntryRepository;
import com.ward.ward_server.api.user.entity.UserEntity;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.api.webcrawling.ItemRepository;
import com.ward.ward_server.api.webcrawling.entity.Item;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ward.ward_server.global.exception.ExceptionCode.ITEM_NOT_FOUND;
import static com.ward.ward_server.global.exception.ExceptionCode.USER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EntryService {
    private final EntryRepository entryRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long createEntry(EntryRequestDTO entryRequestDTO) {

        // UserEntity userEntity = userRepository.findById(entryRequestDTO.getUserId()).get();
        UserEntity userEntity = userRepository.findById(entryRequestDTO.getUserId())
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Item item = itemRepository.findById(entryRequestDTO.getItemId())
                .orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));

        EntryRecord entryRecord = new EntryRecord(userEntity,item);
        entryRepository.save(entryRecord);

        return entryRecord.getEntryId();
    }

//    public EntryRecord getEntryById(Long entryId) {
//        // entryId를 사용하여 특정 응모 내역을 조회하고 반환
//        return entryRepository.findById(entryId).orElse(null);
//    }
//
//    public List<EntryRecord> getUsersEntryRecord(Long userId) {
//        // userId를 사용하여 특정 사용자의 전체 응모 내역 조회하고 반환
//
//        return entryRepository.findAllByUserId(userId);
//    }
//
//    @Transactional
//    public void deleteEntry(Long entryId) {
//        // entryId를 사용하여 특정 응모 내역을 삭제
//        entryRepository.deleteById(entryId);
//    }
}
