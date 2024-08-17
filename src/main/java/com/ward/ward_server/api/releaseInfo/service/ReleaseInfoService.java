package com.ward.ward_server.api.releaseInfo.service;

import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.dto.ExpiringItemResponse;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoDetailResponse;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.entity.enums.CurrencyUnit;
import com.ward.ward_server.api.releaseInfo.entity.enums.DeliveryMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.NotificationMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod;
import com.ward.ward_server.api.releaseInfo.repository.DrawPlatformRepository;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.Object.enums.Section;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.ward.ward_server.global.Object.Constants.API_PAGE_SIZE;
import static com.ward.ward_server.global.exception.ExceptionCode.*;
import static com.ward.ward_server.global.response.error.ErrorMessage.REQUIRED_FIELDS_MUST_BE_PROVIDED;
import static com.ward.ward_server.global.response.error.ErrorMessage.SECTION_NOT_AVAILABLE_THIS_PAGE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        DrawPlatform platform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
        if (releaseInfoRepository.existsByItemIdAndDrawPlatform(item.getId(), platform)) {
            throw new ApiException(DUPLICATE_RELEASE_INFO);
        }
        ReleaseInfo savedReleaseInfo = releaseInfoRepository.save(ReleaseInfo.builder()
                .item(item)
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

    public ReleaseInfoDetailResponse getReleaseInfo(Long releaseInfoId) {
        ReleaseInfo releaseInfo = releaseInfoRepository.findById(releaseInfoId).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        return getDetailResponse(releaseInfo.getItem().getBrand(), releaseInfo.getItem(), releaseInfo.getDrawPlatform(), releaseInfo);
    }

    public List<ReleaseInfoSimpleResponse> getReleaseInfo10List(Long userId, Section section, Category category) {
        return switch (section){
            case DUE_TODAY, RELEASE_WISH, REGISTER_TODAY -> releaseInfoRepository.getReleaseInfo10List(userId, LocalDateTime.now().minusHours(9), category, section); //HACK DB 시간 설정 전까지는 -9시간으로 비교해야 한다.
            default -> throw new ApiException(INVALID_INPUT, SECTION_NOT_AVAILABLE_THIS_PAGE.getMessage());
        };
    }

    public PageResponse<ReleaseInfoSimpleResponse> getReleaseInfoPage(Long userId, Section section, Category category, int page) {
        return switch (section){
            case DUE_TODAY, RELEASE_NOW, REGISTER_TODAY ->{
                Page<ReleaseInfoSimpleResponse> releaseInfoPage = releaseInfoRepository.getReleaseInfoPage(userId, LocalDateTime.now().minusHours(9), category, section, PageRequest.of(page, API_PAGE_SIZE)); //HACK DB 시간 설정 전까지는 -9시간으로 비교해야 한다.
                yield  new PageResponse<>(releaseInfoPage.getContent(), releaseInfoPage);
            }
            default -> throw new ApiException(INVALID_INPUT, SECTION_NOT_AVAILABLE_THIS_PAGE.getMessage());
        };
    }

    public Page<ReleaseInfo> getOngoingReleaseInfos(Long itemId, int page, int size) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();
        return releaseInfoRepository.findByItemAndDueDateAfter(item, now, PageRequest.of(page, size));
    }

    public Page<ReleaseInfo> getCompletedReleaseInfos(Long itemId, int page, int size) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        LocalDateTime now = LocalDateTime.now();
        return releaseInfoRepository.findByItemAndDueDateBefore(item, now, PageRequest.of(page, size));
    }

    @Transactional
    public ReleaseInfoDetailResponse updateReleaseInfo(Long originId,
                                                       Long itemId, String platformName, String siteUrl,
                                                       String releaseDate, String dueDate, String presentationDate, Integer releasePrice, CurrencyUnit currencyUnit,
                                                       NotificationMethod notificationMethod, ReleaseMethod releaseMethod, DeliveryMethod deliveryMethod) {
        ReleaseInfo origin = releaseInfoRepository.findById(originId).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        Item originItem = origin.getItem();
        DrawPlatform originPlatform = origin.getDrawPlatform();
        Item targetItem = null;
        DrawPlatform targetPlatform = null;
        if (itemId != null && !StringUtils.hasText(platformName)) {
            //상품만 변경
            targetItem = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
            if (releaseInfoRepository.existsByItemIdAndDrawPlatform(targetItem.getId(), originPlatform)) {
                throw new ApiException(DUPLICATE_RELEASE_INFO);
            }
            origin.updateItem(targetItem);
        } else if (itemId != null && StringUtils.hasText(platformName)) {
            //상품과 플랫폼 변경
            targetItem = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
            targetPlatform = drawPlatformRepository.findByName(platformName).orElseThrow(() -> new ApiException(DRAW_PLATFORM_NOT_FOUND));
            if (releaseInfoRepository.existsByItemIdAndDrawPlatform(targetItem.getId(), targetPlatform)) {
                throw new ApiException(DUPLICATE_RELEASE_INFO);
            }
            origin.updateItem(targetItem);
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
    public void deleteReleaseInfo(Long releaseInfoId) {
        ReleaseInfo releaseInfo = releaseInfoRepository.findById(releaseInfoId).orElseThrow(() -> new ApiException(RELEASE_INFO_NOT_FOUND));
        releaseInfoRepository.delete(releaseInfo);
    }

    private ReleaseInfoDetailResponse getDetailResponse(Brand brand, Item item, DrawPlatform platform, ReleaseInfo releaseInfo) {
        return new ReleaseInfoDetailResponse(
                releaseInfo.getId(),
                brand.getKoreanName(), brand.getEnglishName(),
                item.getId(), item.getCode(), item.getKoreanName(), item.getEnglishName(), item.getMainImage(),
                platform.getLogoImage(), platform.getKoreanName(), platform.getEnglishName(),
                releaseInfo.getReleaseFormatDate(), releaseInfo.getDueFormatDate(), releaseInfo.getPresentationFormatDate(),
                releaseInfo.getReleasePrice(), releaseInfo.getCurrencyUnit().toString(),
                releaseInfo.getNotificationMethod().getDesc(), releaseInfo.getReleaseMethod().getDesc(), releaseInfo.getDeliveryMethod().getDesc());
    }

    public List<ExpiringItemResponse> getExpiringItems(int limit) {
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, limit);

        return releaseInfoRepository.findExpiringItems(now, pageable);
    }
}
