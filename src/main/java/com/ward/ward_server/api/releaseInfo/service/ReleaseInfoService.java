package com.ward.ward_server.api.releaseInfo.service;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.enumtype.Status;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.entity.enums.DeliveryMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.NotificationMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod;
import com.ward.ward_server.api.releaseInfo.repository.DrawPlatformRepository;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.ward.ward_server.global.exception.ExceptionCode.*;
import static com.ward.ward_server.global.response.error.ErrorCode.ITEM_IDENTIFY_BY_ITEM_CODE_AND_BRAND_NAME;
import static com.ward.ward_server.global.response.error.ErrorCode.REQUIRED_FIELDS_MUST_BE_PROVIDED;

//FIXME 개선필요 : 상품 식별하는 방법을 바꾸기. 너무 난잡함
@Service
@RequiredArgsConstructor
public class ReleaseInfoService {

    private final ReleaseInfoRepository releaseInfoRepository;
    private final DrawPlatformRepository drawPlatformRepository;
    private final ItemRepository itemRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public ReleaseInfo createReleaseInfo(String itemCode, String brandName, String platformName, String siteUrl,
                                         String releaseDate, String dueDate, String presentationDate,
                                         NotificationMethod notificationMethod, ReleaseMethod releaseMethod, DeliveryMethod deliveryMethod) {
        if (!StringUtils.hasText(itemCode) || !StringUtils.hasText(brandName)
                || !StringUtils.hasText(platformName)
                || !StringUtils.hasText(releaseDate)
                || notificationMethod == null || releaseMethod == null || deliveryMethod == null)
            throw new ApiException(INVALID_INPUT, REQUIRED_FIELDS_MUST_BE_PROVIDED.getMessage());
        Long brandId = brandRepository.findIdByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Long itemId = itemRepository.findIdByCodeAndBrandId(itemCode, brandId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform platform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        if(releaseInfoRepository.existsByItemIdAndDrawPlatform(itemId, platform)) throw new ApiException(DUPLICATE_RELEASE_INFO);

        return releaseInfoRepository.save(ReleaseInfo.builder()
                .itemId(itemId)
                .drawPlatform(platform)
                .siteUrl(siteUrl)
                .dueDate(dueDate)
                .presentationDate(presentationDate)
                .releaseDate(releaseDate)
                .status(Status.POSSIBLE) //FIXME 가능으로 고정 설정 -> 추후 수정 필요
                .notificationMethod(notificationMethod)
                .releaseMethod(releaseMethod)
                .deliveryMethod(deliveryMethod)
                .build());
    }

    @Transactional(readOnly = true)
    public ReleaseInfo getReleaseInfo(String itemCode, String brandName, String platformName) {
        Long brandId = brandRepository.findIdByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Long itemId = itemRepository.findIdByCodeAndBrandId(itemCode, brandId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform platform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        return releaseInfoRepository.findByItemIdAndDrawPlatform(itemId, platform).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<ReleaseInfo> getReleaseInfos(Long itemId, Status status, int page, int size) {
        return releaseInfoRepository.findByItemIdAndStatus(itemId, status, PageRequest.of(page, size));
    }

    @Transactional
    public ReleaseInfo updateReleaseInfo(String originItemCode, String originBrandName, String originDrawPlatformName,
                                         String itemCode, String brandName, String platformName, String siteUrl,
                                         String releaseDate, String dueDate, String presentationDate,
                                         NotificationMethod notificationMethod, ReleaseMethod releaseMethod, DeliveryMethod deliveryMethod) {
        Long originBrandId = brandRepository.findIdByName(originBrandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Item originItem = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(originItemCode, originBrandId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform originDrawPlatform = drawPlatformRepository.findByName(originDrawPlatformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        ReleaseInfo origin = releaseInfoRepository.findByItemIdAndDrawPlatform(originItem.getId(), originDrawPlatform).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));

        if ((itemCode == null && brandName != null) || (itemCode != null && brandName == null))  //상품 식별에는 상품코드와 브랜드명이 필요
            throw new ApiException(INVALID_INPUT, ITEM_IDENTIFY_BY_ITEM_CODE_AND_BRAND_NAME.getMessage());

        if (StringUtils.hasText(itemCode) && StringUtils.hasText(brandName) && platformName==null) { //상품(상품코드&브랜드명)만 변경
            Long targetBrandId = brandRepository.findIdByName(originBrandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            Long targetItemId = itemRepository.findIdByCodeAndBrandId(itemCode, targetBrandId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
            if(releaseInfoRepository.existsByItemIdAndDrawPlatform(targetItemId, originDrawPlatform)) throw new ApiException(DUPLICATE_RELEASE_INFO);
            origin.updateItemId(targetItemId);
        }else if (StringUtils.hasText(itemCode) && StringUtils.hasText(brandName) && StringUtils.hasText(platformName)) { //상품(상품코드&브랜드명)과 플랫폼 변경
            Long targetBrandId = brandRepository.findIdByName(originBrandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            Long targetItemId = itemRepository.findIdByCodeAndBrandId(itemCode, targetBrandId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
            DrawPlatform targetPlatform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
            if(releaseInfoRepository.existsByItemIdAndDrawPlatform(targetItemId, targetPlatform)) throw new ApiException(DUPLICATE_RELEASE_INFO);
            origin.updateItemId(targetItemId);
        }else if(StringUtils.hasText(platformName)){ //플랫폼만 변경
            DrawPlatform targetPlatform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
            if(releaseInfoRepository.existsByItemIdAndDrawPlatform(originItem.getId(), targetPlatform)) throw new ApiException(DUPLICATE_RELEASE_INFO);
            origin.updateDrawPlatform(targetPlatform);
        }
        if (StringUtils.hasText(siteUrl)) origin.updateSiteUrl(siteUrl);
        if (StringUtils.hasText(releaseDate)) origin.updateReleaseDate(releaseDate);
        if (StringUtils.hasText(dueDate)) origin.updateDueDate(dueDate);
        if (StringUtils.hasText(presentationDate)) origin.updatePresentationDate(presentationDate);
        if (notificationMethod != null) origin.updateNotificationMethod(notificationMethod);
        if (releaseMethod != null) origin.updateReleaseMethod(releaseMethod);
        if (deliveryMethod != null) origin.updateDeliveryMethod(deliveryMethod);
        return origin;
    }

    @Transactional
    public void deleteReleaseInfo(String itemCode, String brandName, String platformName) {
        Long brandId = brandRepository.findIdByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Long itemId = itemRepository.findIdByCodeAndBrandId(itemCode, brandId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform platform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        releaseInfoRepository.deleteByItemIdAndDrawPlatform(itemId, platform);
    }

}
