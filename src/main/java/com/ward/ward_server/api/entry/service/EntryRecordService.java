package com.ward.ward_server.api.entry.service;

import com.ward.ward_server.api.entry.dto.EntryRecordDetailResponse;
import com.ward.ward_server.api.entry.dto.EntryRecordRequest;
import com.ward.ward_server.api.entry.dto.EntryRecordResponse;
import com.ward.ward_server.api.entry.entity.EntryRecord;
import com.ward.ward_server.api.entry.repository.EntryRecordRepository;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.enumtype.Status;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.repository.DrawPlatformRepository;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntryRecordService {
    private final EntryRecordRepository entryRecordRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final DrawPlatformRepository drawPlatformRepository;
    private final ReleaseInfoRepository releaseInfoRepository;

    public EntryRecordResponse createEntryRecord(Long userId, EntryRecordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(request.itemCode())
                .orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        //FIXME Query 개선필요
        DrawPlatform drawPlatform = drawPlatformRepository.findByName(request.platformName())
                .orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        ReleaseInfo releaseInfo = releaseInfoRepository.findByItemIdAndDrawPlatform(item.getId(), drawPlatform)
                .orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        if(releaseInfo.getStatus().equals(Status.IMPOSSIBLE)) throw new ApiException(RELEASE_INFO_EXPIRED);
        if (entryRecordRepository.existsByUserIdAndReleaseInfoId(user.getId(), releaseInfo.getId()))
            throw new ApiException(DUPLICATE_ENTRY_RECORD);
        EntryRecord savedEntryRecord = entryRecordRepository.save(new EntryRecord(user, releaseInfo, request.memo()));
        return new EntryRecordResponse(savedEntryRecord.getEntryDate(), savedEntryRecord.getMemo());
    }

    @Transactional(readOnly = true)
    public List<EntryRecordDetailResponse> getEntryRecordListByItem(Long userId, String itemCode) {
        if (userId == null) throw new ApiException(USER_NOT_FOUND);
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(itemCode)
                .orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        List<ReleaseInfo> releaseInfos = releaseInfoRepository.findAllByItemId(item.getId());
        if (releaseInfos.isEmpty()) throw new ApiException(RELEASE_INFO_NOT_FOUND);
        Map<ReleaseInfo, List<Object>> entryRecords = entryRecordRepository.findAllByUserIdAndReleaseInfoIdIn(
                        userId, releaseInfos.stream().map(ReleaseInfo::getId).toList())
                .stream()
                .collect(
                        Collectors.toMap(
                                EntryRecord::getReleaseInfo,
                                entryRecord -> Arrays.asList(true, entryRecord.getEntryDate(), entryRecord.getMemo())
                        ));
        final List<Object> NOT_ENTRY = Arrays.asList(false, "", "");
        //FIXME Query 개선필요
        return releaseInfos.stream()
                .map(info -> new EntryRecordDetailResponse(
                        entryRecords.getOrDefault(info, NOT_ENTRY).get(0).equals(true),
                        entryRecords.getOrDefault(info, NOT_ENTRY).get(1).toString(),
                        entryRecords.getOrDefault(info, NOT_ENTRY).get(2).toString(),
                        new ReleaseInfoSimpleResponse(
                                info.getDrawPlatform().getLogoImage(),
                                info.getDrawPlatform().getName(),
                                info.getSiteUrl(),
                                info.getDueDate(),
                                info.getStatus().toString()
                        )))
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<EntryRecordDetailResponse> getEntryRecordListByUser(Long userId, int page, int size) {
        if (userId == null) throw new ApiException(USER_NOT_FOUND);
        Page<EntryRecord> entryRecordPage = entryRecordRepository.findAllByUserId(userId, PageRequest.of(page, size));
        List<EntryRecord> contents = entryRecordPage.getContent();
        List<EntryRecordDetailResponse> responses = contents.stream()
                .map(record -> new EntryRecordDetailResponse(true, record.getEntryDate(), record.getMemo(),
                        new ReleaseInfoSimpleResponse(
                                record.getReleaseInfo().getDrawPlatform().getLogoImage(),
                                record.getReleaseInfo().getDrawPlatform().getName(),
                                record.getReleaseInfo().getSiteUrl(),
                                record.getReleaseInfo().getDueDate(),
                                record.getReleaseInfo().getStatus().toString())))
                .toList();
        return new PageResponse<>(responses, entryRecordPage);
    }

    @Transactional
    public void deleteEntryRecord(Long userId, String itemCode, String platformName) {
        if (userId == null) throw new ApiException(USER_NOT_FOUND);
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(itemCode)
                .orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform drawPlatform = drawPlatformRepository.findByName(platformName)
                .orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        ReleaseInfo releaseInfo = releaseInfoRepository.findByItemIdAndDrawPlatform(item.getId(), drawPlatform)
                .orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        if (!entryRecordRepository.existsByUserIdAndReleaseInfoId(userId, releaseInfo.getId()))
            throw new ApiException(ENTRY_RECORD_NOT_FOUND);
        entryRecordRepository.deleteByUserIdAndReleaseInfoId(userId, releaseInfo.getId());
    }
}