package com.ward.ward_server.api.entry.service;

import com.ward.ward_server.api.entry.dto.EntryCountResponse;
import com.ward.ward_server.api.entry.dto.EntryDetailResponse;
import com.ward.ward_server.api.entry.dto.EntryRecordResponse;
import com.ward.ward_server.api.entry.entity.EntryRecord;
import com.ward.ward_server.api.entry.repository.EntryRecordRepository;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EntryRecordService {
    private final EntryRecordRepository entryRecordRepository;
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

    public EntryRecordResponse getEntryRecordByReleaseInfo(Long userId, Long releaseInfoId) {
        Optional<EntryRecord> entryRecord = entryRecordRepository.findByUserIdAndReleaseInfoId(userId, releaseInfoId);
        return entryRecord.map(record -> new EntryRecordResponse(true, record.getEntryDate())).orElseGet(() -> new EntryRecordResponse(false, null));
    }

    public EntryCountResponse getEntryCounts(Long userId) {
        long total = entryRecordRepository.countByUserId(userId);
        long inProgress = entryRecordRepository.countInProgressByUserId(userId); // 진행
        long announcement = entryRecordRepository.countAnnouncementByUserId(userId); // 당첨자 발표
        long closed = entryRecordRepository.countClosedByUserId(userId); // 마감

        return new EntryCountResponse(total, inProgress, announcement, closed);
    }

    @Transactional
    public void deleteEntryRecord(Long userId, Long releaseInfoId) {
        if (!entryRecordRepository.existsByUserIdAndReleaseInfoId(userId, releaseInfoId)) {
            throw new ApiException(ENTRY_RECORD_NOT_FOUND);
        }
        entryRecordRepository.deleteByUserIdAndReleaseInfoId(userId, releaseInfoId);
    }

    public Page<EntryDetailResponse> getEntryRecordsByStatus(Long userId, String status, Pageable pageable) {
        return switch (status) {
            case "in-progress" -> entryRecordRepository.findInProgressByUserId(userId, pageable);
            case "announcement" -> entryRecordRepository.findAnnouncementByUserId(userId, pageable);
            case "closed" -> entryRecordRepository.findClosedByUserId(userId, pageable);
            default -> entryRecordRepository.findAllByUserId(userId, pageable);
        };
    }

}
