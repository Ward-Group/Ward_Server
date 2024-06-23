package com.ward.ward_server.api.entry.service;

import com.ward.ward_server.api.entry.dto.EntryRecordResponse;
import com.ward.ward_server.api.entry.entity.EntryRecord;
import com.ward.ward_server.api.entry.repository.EntryRecordRepository;
import com.ward.ward_server.api.releaseInfo.repository.DrawPlatformRepository;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
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

    @Transactional
    public EntryRecordResponse createEntryRecord(Long userId, Long itemId, String platformName) {
        long drawPlatformId = drawPlatformRepository.findIdByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        long releaseInfoId = releaseInfoRepository.findIdByItemIdAndDrawPlatformId(itemId, drawPlatformId).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        if (entryRecordRepository.existsByUserIdAndReleaseInfoId(userId, releaseInfoId)) {
            throw new ApiException(DUPLICATE_ENTRY_RECORD);
        }
        EntryRecord savedEntryRecord = entryRecordRepository.save(new EntryRecord(userId, releaseInfoId));
        return new EntryRecordResponse(true, savedEntryRecord.getEntryDate());
    }

    @Transactional(readOnly = true)
    public EntryRecordResponse getEntryRecordByReleaseInfo(Long userId, Long itemId, String platformName) {
        long drawPlatformId = drawPlatformRepository.findIdByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        long releaseInfoId = releaseInfoRepository.findIdByItemIdAndDrawPlatformId(itemId, drawPlatformId).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        Optional<EntryRecord> entryRecord = entryRecordRepository.findByUserIdAndReleaseInfoId(userId, releaseInfoId);
        return entryRecord.map(record -> new EntryRecordResponse(true, record.getEntryDate())).orElseGet(() -> new EntryRecordResponse(false, null));
    }

    @Transactional
    public void deleteEntryRecord(Long userId, Long itemId, String platformName) {
        long drawPlatformId = drawPlatformRepository.findIdByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        long releaseInfoId = releaseInfoRepository.findIdByItemIdAndDrawPlatformId(itemId, drawPlatformId).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        if (!entryRecordRepository.existsByUserIdAndReleaseInfoId(userId, releaseInfoId)) {
            throw new ApiException(ENTRY_RECORD_NOT_FOUND);
        }
        entryRecordRepository.deleteByUserIdAndReleaseInfoId(userId, releaseInfoId);
    }
}
