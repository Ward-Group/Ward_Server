package com.ward.ward_server.api.releaseInfo.service;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.enumtype.Status;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoRequest;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoDetailResponse;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class ReleaseInfoService {

    private final ReleaseInfoRepository releaseInfoRepository;
    private final ItemRepository itemRepository;
    private final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ReleaseInfoDetailResponse createReleaseInfo(ReleaseInfoRequest request) {
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(request.itemCode()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        if (releaseInfoRepository.existsByItemIdAndDrawPlatform(item.getId(), request.drawPlatform()))
            throw new ApiException(DUPLICATE_DRAW_PLATFORM);
        LocalDateTime releaseDate = LocalDateTime.parse(request.releaseDate(), FORMAT);
        LocalDateTime dueDate = request.dueDate() != null ? LocalDateTime.parse(request.dueDate(), FORMAT) : null;
        LocalDateTime presentationDate = request.presentationDate() != null ? LocalDateTime.parse(request.presentationDate(), FORMAT) : null;
        ReleaseInfo savedInfo = releaseInfoRepository.save(ReleaseInfo.builder()
                .itemId(item.getId())
                .drawPlatform(request.drawPlatform())
                .siteUrl(request.siteUrl())
                .releaseDate(releaseDate)
                .dueDate(dueDate)
                .presentationDate(presentationDate)
                .status(Status.of(releaseDate, dueDate)).build());

        return getDetailResponse(item, savedInfo);
    }

    public List<ReleaseInfoSimpleResponse> getReleaseInfoList(String itemCode) {
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(itemCode).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        //FIXME 일단 응모 다 false로 반환중. entry고치면서 고쳐야한다.
        return releaseInfoRepository.findAllByItemId(item.getId()).stream()
                .map(e -> new ReleaseInfoSimpleResponse(item.getItemImages().get(0).getUrl(), e.getDrawPlatform(), e.getSiteUrl(), e.getDueDate(), Status.of(e.getReleaseDate(), e.getDueDate()).toString(), false))
                .collect(Collectors.toList());
    }

    @Transactional
    public ReleaseInfoDetailResponse updateReleaseInfo(String itemCode, String drawPlatform, ReleaseInfoRequest request) {
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(itemCode).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        ReleaseInfo releaseInfo = releaseInfoRepository.findByItemIdAndDrawPlatform(item.getId(), drawPlatform).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        Item targetItem = null;
        if (request.itemCode() != null && !request.itemCode().isBlank()) {
            targetItem = itemRepository.findByCodeAndDeletedAtIsNull(request.itemCode()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
            releaseInfo.updateItemId(targetItem.getId());
        }
        if (request.drawPlatform() != null && !request.drawPlatform().isBlank()) {
            if (releaseInfoRepository.existsByItemIdAndDrawPlatform(item.getId(), request.drawPlatform()))
                throw new ApiException(DUPLICATE_DRAW_PLATFORM);
            releaseInfo.updateDrawPlatform(request.drawPlatform());
        }
        if (request.siteUrl() != null && !request.siteUrl().isBlank())
            releaseInfo.updateSiteUrl(request.siteUrl());
        if (request.releaseDate() != null && !request.releaseDate().isBlank())
            releaseInfo.updateReleaseDate(LocalDateTime.parse(request.releaseDate(), FORMAT));
        if (request.dueDate() != null && !request.dueDate().isBlank())
            releaseInfo.updateDueDate(LocalDateTime.parse(request.dueDate(), FORMAT));
        if (request.presentationDate() != null && !request.presentationDate().isBlank())
            releaseInfo.updatePresentationDate(LocalDateTime.parse(request.presentationDate(), FORMAT));
        return getDetailResponse(targetItem == null ? item : targetItem, releaseInfo);
    }

    @Transactional
    public void deleteReleaseInfo(String itemCode, String drawPlatform) {
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(itemCode).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        releaseInfoRepository.deleteByItemIdAndDrawPlatform(item.getId(), drawPlatform);
    }

    private ReleaseInfoDetailResponse getDetailResponse(Item item, ReleaseInfo info) {
        return new ReleaseInfoDetailResponse(item.getCode(), item.getName(), item.getPrice(),
                info.getDrawPlatform(),
                info.getSiteUrl(),
                info.getReleaseDate(),
                info.getDueDate(),
                info.getPresentationDate(),
                info.getStatus().toString());
    }
}
