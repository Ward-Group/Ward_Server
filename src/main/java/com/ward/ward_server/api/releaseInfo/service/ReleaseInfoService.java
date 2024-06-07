package com.ward.ward_server.api.releaseInfo.service;

import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.enumtype.Status;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoDetailResponse;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoRequest;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.repository.DrawPlatformRepository;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ward.ward_server.global.Object.Constants.FORMAT;
import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class ReleaseInfoService {

    private final ReleaseInfoRepository releaseInfoRepository;
    private final DrawPlatformRepository drawPlatformRepository;
    private final ItemRepository itemRepository;
    private final BrandRepository brandRepository;

    public ReleaseInfoDetailResponse createReleaseInfo(ReleaseInfoRequest request) {
        Brand brand = brandRepository.findByName(request.brandName()).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Item item = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(request.itemCode(), brand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        Optional<DrawPlatform> drawPlatform = drawPlatformRepository.findByName(request.platformName());
        if (drawPlatform.isEmpty() && request.platformLogoImage() != null && !request.platformLogoImage().isBlank()) { //등록되지 않은 발매플랫폼 && 로고이미지가 null도 아니고 공백도 아니라면 -> 지금 등록한다.
            drawPlatform = Optional.of(drawPlatformRepository.save(
                    DrawPlatform.builder()
                            .name(request.platformName())
                            .logoImage(request.platformLogoImage())
                            .build()));
        } else if (drawPlatform.isEmpty() && request.platformLogoImage() == null) {
            throw new ApiException(INVALID_INPUT);
        }
        if (releaseInfoRepository.existsByItemIdAndDrawPlatform(item.getId(), drawPlatform.get()))
            throw new ApiException(DUPLICATE_RELEASE_INFO);
        LocalDateTime releaseDate = LocalDateTime.parse(request.releaseDate(), FORMAT);
        LocalDateTime dueDate = request.dueDate() != null ? LocalDateTime.parse(request.dueDate(), FORMAT) : null;
        LocalDateTime presentationDate = request.presentationDate() != null ? LocalDateTime.parse(request.presentationDate(), FORMAT) : null;
        ReleaseInfo savedInfo = releaseInfoRepository.save(ReleaseInfo.builder()
                .itemId(item.getId())
                .drawPlatform(drawPlatform.get())
                .siteUrl(request.siteUrl())
                .releaseDate(releaseDate)
                .dueDate(dueDate)
                .presentationDate(presentationDate)
                .status(Status.of(releaseDate, dueDate)).build());
        return getDetailResponse(item, savedInfo);
    }

    //FIXME 회원정보를 기반으로 entry 정보 넘겨야한다.
    public List<ReleaseInfoSimpleResponse> getReleaseInfoList(String itemCode, String brandName) {
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Item item = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(itemCode, brand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        return releaseInfoRepository.findAllByItemId(item.getId()).stream()
                .map(e -> new ReleaseInfoSimpleResponse(e.getDrawPlatform().getLogoImage(), e.getDrawPlatform().getName(), e.getSiteUrl(), e.getDueDate(), Status.of(e.getReleaseLocalDate(), e.getDueLocalDate()).toString()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ReleaseInfoDetailResponse updateReleaseInfo(String itemCode, String brandName, String originDrawPlatformName, ReleaseInfoRequest request) {
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Item originItem = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(itemCode, brand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform originDrawPlatform = drawPlatformRepository.findByName(originDrawPlatformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        ReleaseInfo releaseInfo = releaseInfoRepository.findByItemIdAndDrawPlatform(originItem.getId(), originDrawPlatform).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        Item targetItem = null;
        if (request.itemCode() != null && !request.itemCode().isBlank()) {
            Brand targetBrand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            targetItem = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(request.itemCode(), targetBrand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
            releaseInfo.updateItemId(targetItem.getId());
        }
        if (request.platformName() != null && !request.platformName().isBlank()) {
            Optional<DrawPlatform> targetDrawPlatform = drawPlatformRepository.findByName(originDrawPlatformName);
            if (targetDrawPlatform.isEmpty() && request.platformLogoImage() != null && !request.platformLogoImage().isBlank()) {
                targetDrawPlatform = Optional.of(drawPlatformRepository.save(
                        DrawPlatform.builder()
                                .name(request.platformName())
                                .logoImage(request.platformLogoImage())
                                .build()));
            } else if (targetDrawPlatform.isEmpty() && request.platformLogoImage() == null) {
                throw new ApiException(INVALID_INPUT);
            }
            if (releaseInfoRepository.existsByItemIdAndDrawPlatform(releaseInfo.getItemId(), targetDrawPlatform.get()))
                throw new ApiException(DUPLICATE_RELEASE_INFO);
            releaseInfo.updateDrawPlatform(targetDrawPlatform.get());
        }
        if (request.siteUrl() != null && !request.siteUrl().isBlank()) releaseInfo.updateSiteUrl(request.siteUrl());
        if (request.releaseDate() != null && !request.releaseDate().isBlank())
            releaseInfo.updateReleaseDate(request.releaseDate());
        if (request.dueDate() != null && !request.dueDate().isBlank())
            releaseInfo.updateDueDate(request.dueDate());
        if (request.presentationDate() != null && !request.presentationDate().isBlank())
            releaseInfo.updatePresentationDate(request.presentationDate());
        return getDetailResponse(targetItem == null ? originItem : targetItem, releaseInfo);
    }

    @Transactional
    public void deleteReleaseInfo(String itemCode, String brandName, String drawPlatformName) {
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Item item = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(itemCode, brand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform drawPlatform = drawPlatformRepository.findByName(drawPlatformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        releaseInfoRepository.deleteByItemIdAndDrawPlatform(item.getId(), drawPlatform);
    }

    private ReleaseInfoDetailResponse getDetailResponse(Item item, ReleaseInfo info) {
        return new ReleaseInfoDetailResponse(
                item.getCode(), item.getKoreanName(), item.getPrice(),
                info.getDrawPlatform().getLogoImage(),
                info.getDrawPlatform().getName(),
                info.getSiteUrl(),
                info.getReleaseDate(),
                info.getDueDate(),
                info.getPresentationDate(),
                info.getStatus().toString());
    }
}
