package com.ward.ward_server.api.entry.service;

import com.ward.ward_server.api.entry.dto.EntryRecordResponse;
import com.ward.ward_server.api.entry.entity.EntryRecord;
import com.ward.ward_server.api.entry.repository.EntryRecordRepository;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.repository.DrawPlatformRepository;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class EntryRecordService {
    private final EntryRecordRepository entryRecordRepository;
    private final DrawPlatformRepository drawPlatformRepository;
    private final ReleaseInfoRepository releaseInfoRepository;
    private final UserRepository userRepository;

    @Transactional
    public EntryRecordResponse createEntryRecord(Long userId, Long releaseInfoId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ApiException(USER_NOT_FOUND));
        ReleaseInfo releaseInfo=releaseInfoRepository.findById(releaseInfoId).orElseThrow(()->new ApiException(RELEASE_INFO_NOT_FOUND));
        if (entryRecordRepository.existsByUserIdAndReleaseInfoId(userId, releaseInfoId)) {
            throw new ApiException(DUPLICATE_ENTRY_RECORD);
        }
        EntryRecord savedEntryRecord = entryRecordRepository.save(new EntryRecord(user, releaseInfo));
        return new EntryRecordResponse(true, savedEntryRecord.getEntryDate());
    }

    @Transactional(readOnly = true)
    public EntryRecordResponse getEntryRecordByReleaseInfo(Long userId, Long releaseInfoId) {
        Optional<EntryRecord> entryRecord = entryRecordRepository.findByUserIdAndReleaseInfoId(userId, releaseInfoId);
        return entryRecord.map(record -> new EntryRecordResponse(true, record.getEntryDate())).orElseGet(() -> new EntryRecordResponse(false, null));
    }

    @Transactional
    public void deleteEntryRecord(Long userId, Long releaseInfoId) {
        if (!entryRecordRepository.existsByUserIdAndReleaseInfoId(userId, releaseInfoId)) {
            throw new ApiException(ENTRY_RECORD_NOT_FOUND);
        }
        entryRecordRepository.deleteByUserIdAndReleaseInfoId(userId, releaseInfoId);
    }
}
