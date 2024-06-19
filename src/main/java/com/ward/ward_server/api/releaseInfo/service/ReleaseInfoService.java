package com.ward.ward_server.api.releaseInfo.service;

import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoDetailResponse;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.entity.enums.CurrencyUnit;
import com.ward.ward_server.api.releaseInfo.entity.enums.DeliveryMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.NotificationMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod;
import com.ward.ward_server.api.releaseInfo.repository.DrawPlatformRepository;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.ward.ward_server.global.exception.ExceptionCode.*;
import static com.ward.ward_server.global.response.error.ErrorCode.ITEM_IDENTIFY_BY_ITEM_CODE_AND_BRAND_NAME;
import static com.ward.ward_server.global.response.error.ErrorCode.REQUIRED_FIELDS_MUST_BE_PROVIDED;

@Service
@RequiredArgsConstructor
public class ReleaseInfoService {

    private final ReleaseInfoRepository releaseInfoRepository;
    private final DrawPlatformRepository drawPlatformRepository;
    private final ItemRepository itemRepository;
    private final BrandRepository brandRepository;

    @Transactional
    public ReleaseInfoDetailResponse createReleaseInfo(String itemCode, String brandName, String platformName, String siteUrl,
                                                       String releaseDate, String dueDate, String presentationDate, Integer releasePrice, CurrencyUnit currencyUnit,
                                                       NotificationMethod notificationMethod, ReleaseMethod releaseMethod, DeliveryMethod deliveryMethod) {
        if (!StringUtils.hasText(itemCode) || !StringUtils.hasText(brandName)
                || !StringUtils.hasText(platformName)
                || !StringUtils.hasText(releaseDate)
                || notificationMethod == null || releaseMethod == null || deliveryMethod == null)
            throw new ApiException(INVALID_INPUT, REQUIRED_FIELDS_MUST_BE_PROVIDED.getMessage());
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Item item = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(itemCode, brand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform platform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        if (releaseInfoRepository.existsByItemIdAndDrawPlatform(item.getId(), platform))
            throw new ApiException(DUPLICATE_RELEASE_INFO);
        ReleaseInfo savedReleaseInfo = releaseInfoRepository.save(ReleaseInfo.builder()
                .itemId(item.getId())
                .drawPlatform(platform)
                .siteUrl(siteUrl)
                .dueDate(dueDate)
                .presentationDate(presentationDate)
                .releaseDate(releaseDate)
                .releasePrice(releasePrice)
                .currencyUnit(currencyUnit)
                .notificationMethod(notificationMethod)
                .releaseMethod(releaseMethod)
                .deliveryMethod(deliveryMethod)
                .build());
        return getDetailResponse(brand, item, platform, savedReleaseInfo);
    }

    @Transactional(readOnly = true)
    public ReleaseInfoDetailResponse getReleaseInfo(String itemCode, String brandName, String platformName) {
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Item item = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(itemCode, brand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform platform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        ReleaseInfo releaseInfo = releaseInfoRepository.findByItemIdAndDrawPlatform(item.getId(), platform).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        return getDetailResponse(brand, item, platform, releaseInfo);
    }

    @Transactional
    public ReleaseInfoDetailResponse updateReleaseInfo(String originItemCode, String originBrandName, String originDrawPlatformName,
                                                       String itemCode, String brandName, String platformName, String siteUrl,
                                                       String releaseDate, String dueDate, String presentationDate, Integer releasePrice, CurrencyUnit currencyUnit,
                                                       NotificationMethod notificationMethod, ReleaseMethod releaseMethod, DeliveryMethod deliveryMethod) {
        Brand originBrand = brandRepository.findByName(originBrandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Item originItem = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(originItemCode, originBrand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform originPlatform = drawPlatformRepository.findByName(originDrawPlatformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        ReleaseInfo origin = releaseInfoRepository.findByItemIdAndDrawPlatform(originItem.getId(), originPlatform).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));

        if ((itemCode == null && brandName != null) || (itemCode != null && brandName == null))  //상품 식별에는 상품코드와 브랜드명이 필요
            throw new ApiException(INVALID_INPUT, ITEM_IDENTIFY_BY_ITEM_CODE_AND_BRAND_NAME.getMessage());

        Item targetItem = null;
        DrawPlatform targetPlatform = null;
        Brand targetBrand = null;
        if (StringUtils.hasText(itemCode) && StringUtils.hasText(brandName) && platformName == null) {
            //상품(상품코드&브랜드명)만 변경
            targetBrand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            targetItem = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(itemCode, targetBrand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
            if (releaseInfoRepository.existsByItemIdAndDrawPlatform(targetItem.getId(), originPlatform))
                throw new ApiException(DUPLICATE_RELEASE_INFO);
            origin.updateItemId(targetItem.getId());
        } else if (StringUtils.hasText(itemCode) && StringUtils.hasText(brandName) && StringUtils.hasText(platformName)) {
            //상품(상품코드&브랜드명)과 플랫폼 변경
            targetBrand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            targetItem = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(itemCode, targetBrand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
            targetPlatform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
            if (releaseInfoRepository.existsByItemIdAndDrawPlatform(targetItem.getId(), targetPlatform))
                throw new ApiException(DUPLICATE_RELEASE_INFO);
            origin.updateItemId(targetItem.getId());
            origin.updateDrawPlatform(targetPlatform);
        } else if (StringUtils.hasText(platformName)) {
            //플랫폼만 변경
            targetPlatform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
            if (releaseInfoRepository.existsByItemIdAndDrawPlatform(originItem.getId(), targetPlatform))
                throw new ApiException(DUPLICATE_RELEASE_INFO);
            origin.updateDrawPlatform(targetPlatform);
        }

        if (StringUtils.hasText(siteUrl)) origin.updateSiteUrl(siteUrl);
        if (StringUtils.hasText(releaseDate)) origin.updateReleaseDate(releaseDate);
        if (StringUtils.hasText(dueDate)) origin.updateDueDate(dueDate);
        if (StringUtils.hasText(presentationDate)) origin.updatePresentationDate(presentationDate);
        if (releasePrice != null) origin.updateReleasePrice(releasePrice);
        if (currencyUnit != null) origin.updateCurrencyUnit(currencyUnit);
        if (notificationMethod != null) origin.updateNotificationMethod(notificationMethod);
        if (releaseMethod != null) origin.updateReleaseMethod(releaseMethod);
        if (deliveryMethod != null) origin.updateDeliveryMethod(deliveryMethod);

        return getDetailResponse(
                targetBrand == null ? originBrand : targetBrand,
                targetItem == null ? originItem : targetItem,
                targetPlatform == null ? originPlatform : targetPlatform,
                origin);
    }

    @Transactional
    public void deleteReleaseInfo(String itemCode, String brandName, String platformName) {
        Long brandId = brandRepository.findIdByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Long itemId = itemRepository.findIdByCodeAndBrandId(itemCode, brandId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform platform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        releaseInfoRepository.deleteByItemIdAndDrawPlatform(itemId, platform);
    }

    private ReleaseInfoDetailResponse getDetailResponse(Brand brand, Item item, DrawPlatform platform, ReleaseInfo releaseInfo) {
        return new ReleaseInfoDetailResponse(
                brand.getKoreanName(), brand.getEnglishName(),
                item.getCode(), item.getKoreanName(), item.getEnglishName(), item.getMainImage(),
                platform.getLogoImage(), platform.getKoreanName(), platform.getEnglishName(),
                releaseInfo.getReleaseFormatDate(), releaseInfo.getDueFormatDate(), releaseInfo.getPresentationFormatDate(),
                releaseInfo.getReleasePrice(), releaseInfo.getCurrencyUnit().toString(),
                releaseInfo.getNotificationMethod().getDesc(), releaseInfo.getReleaseMethod().getDesc(), releaseInfo.getDeliveryMethod().getDesc());
    }
}
