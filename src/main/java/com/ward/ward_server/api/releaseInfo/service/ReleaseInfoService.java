package com.ward.ward_server.api.releaseInfo.service;

import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.entity.Item;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

import static com.ward.ward_server.global.exception.ExceptionCode.*;
import static com.ward.ward_server.global.response.error.ErrorCode.REQUIRED_FIELDS_MUST_BE_PROVIDED;

@Service
@RequiredArgsConstructor
public class ReleaseInfoService {

    private final ReleaseInfoRepository releaseInfoRepository;
    private final DrawPlatformRepository drawPlatformRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public ReleaseInfoDetailResponse createReleaseInfo(Long itemId, String platformName, String siteUrl,
                                                       String releaseDate, String dueDate, String presentationDate, Integer releasePrice, CurrencyUnit currencyUnit,
                                                       NotificationMethod notificationMethod, ReleaseMethod releaseMethod, DeliveryMethod deliveryMethod) {
        if (itemId == null || !StringUtils.hasText(platformName) || !StringUtils.hasText(releaseDate)
                || notificationMethod == null || releaseMethod == null || deliveryMethod == null) {
            throw new ApiException(INVALID_INPUT, REQUIRED_FIELDS_MUST_BE_PROVIDED.getMessage());
        }
        Item item = itemRepository.findByIdAndDeletedAtIsNull(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform platform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        if (releaseInfoRepository.existsByItemIdAndDrawPlatform(item.getId(), platform)) {
            throw new ApiException(DUPLICATE_RELEASE_INFO);
        }
        ReleaseInfo savedReleaseInfo = releaseInfoRepository.save(ReleaseInfo.builder()
                .itemId(itemId)
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
        return getDetailResponse(item.getBrand(), item, platform, savedReleaseInfo);
    }

    @Transactional(readOnly = true)
    public ReleaseInfoDetailResponse getReleaseInfo(Long itemId, String platformName) {
        Item item = itemRepository.findByIdAndDeletedAtIsNull(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform platform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        ReleaseInfo releaseInfo = releaseInfoRepository.findByItemIdAndDrawPlatform(item.getId(), platform).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        return getDetailResponse(item.getBrand(), item, platform, releaseInfo);
    }

    @Transactional
    public ReleaseInfoDetailResponse updateReleaseInfo(Long originItemId, String originDrawPlatformName,
                                                       Long itemId, String platformName, String siteUrl,
                                                       String releaseDate, String dueDate, String presentationDate, Integer releasePrice, CurrencyUnit currencyUnit,
                                                       NotificationMethod notificationMethod, ReleaseMethod releaseMethod, DeliveryMethod deliveryMethod) {
        Item originItem = itemRepository.findByIdAndDeletedAtIsNull(originItemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform originPlatform = drawPlatformRepository.findByName(originDrawPlatformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        ReleaseInfo origin = releaseInfoRepository.findByItemIdAndDrawPlatform(originItem.getId(), originPlatform).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));

        Item targetItem = null;
        DrawPlatform targetPlatform = null;
        if (itemId != null && !StringUtils.hasText(platformName)) {
            //상품만 변경
            targetItem = itemRepository.findByIdAndDeletedAtIsNull(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
            if (releaseInfoRepository.existsByItemIdAndDrawPlatform(targetItem.getId(), originPlatform)) {
                throw new ApiException(DUPLICATE_RELEASE_INFO);
            }
            origin.updateItemId(targetItem.getId());
        } else if (itemId != null && StringUtils.hasText(platformName)) {
            //상품과 플랫폼 변경
            targetItem = itemRepository.findByIdAndDeletedAtIsNull(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
            targetPlatform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
            if (releaseInfoRepository.existsByItemIdAndDrawPlatform(targetItem.getId(), targetPlatform)) {
                throw new ApiException(DUPLICATE_RELEASE_INFO);
            }
            origin.updateItemId(targetItem.getId());
            origin.updateDrawPlatform(targetPlatform);
        } else if (StringUtils.hasText(platformName)) {
            //플랫폼만 변경
            targetPlatform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
            if (releaseInfoRepository.existsByItemIdAndDrawPlatform(originItem.getId(), targetPlatform)) {
                throw new ApiException(DUPLICATE_RELEASE_INFO);
            }
            origin.updateDrawPlatform(targetPlatform);
        }

        if (StringUtils.hasText(siteUrl)) {
            origin.updateSiteUrl(siteUrl);
        }
        if (StringUtils.hasText(releaseDate)) {
            origin.updateReleaseDate(releaseDate);
        }
        if (StringUtils.hasText(dueDate)) {
            origin.updateDueDate(dueDate);
        }
        if (StringUtils.hasText(presentationDate)) {
            origin.updatePresentationDate(presentationDate);
        }
        if (releasePrice != null) {
            origin.updateReleasePrice(releasePrice);
        }
        if (currencyUnit != null) {
            origin.updateCurrencyUnit(currencyUnit);
        }
        if (notificationMethod != null) {
            origin.updateNotificationMethod(notificationMethod);
        }
        if (releaseMethod != null) {
            origin.updateReleaseMethod(releaseMethod);
        }
        if (deliveryMethod != null) {
            origin.updateDeliveryMethod(deliveryMethod);
        }

        return getDetailResponse(
                targetItem == null ? originItem.getBrand() : targetItem.getBrand(),
                targetItem == null ? originItem : targetItem,
                targetPlatform == null ? originPlatform : targetPlatform,
                origin);
    }

    @Transactional
    public void deleteReleaseInfo(Long itemId, String platformName) {
        DrawPlatform platform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        ReleaseInfo releaseInfo = releaseInfoRepository.findByItemIdAndDrawPlatform(itemId, platform).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        releaseInfo.setDeletedAt(LocalDateTime.now());
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
