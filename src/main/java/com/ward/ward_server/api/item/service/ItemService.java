package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.ItemImage;
import com.ward.ward_server.api.item.entity.ItemViewCount;
import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.item.repository.ItemImageRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.item.repository.ItemViewCountRepository;
import com.ward.ward_server.global.Object.enums.Sort;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ward.ward_server.global.exception.ExceptionCode.*;
import static com.ward.ward_server.global.response.error.ErrorCode.NAME_MUST_BE_PROVIDED;
import static com.ward.ward_server.global.response.error.ErrorCode.REQUIRED_FIELDS_MUST_BE_PROVIDED;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final BrandRepository brandRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemViewCountRepository itemViewCountRepository;

    @Transactional
    public ItemDetailResponse createItem(String itemCode, String koreanName, String englishName, String mainImage, List<String> itemImages, String brandName, Category category, Integer price) throws ApiException {
        if (!StringUtils.hasText(koreanName) && !StringUtils.hasText(englishName)) {
            throw new ApiException(INVALID_INPUT, NAME_MUST_BE_PROVIDED.getMessage());
        }
        ValidationUtils.validationNames(koreanName, englishName);
        if (!StringUtils.hasText(itemCode) || !StringUtils.hasText(brandName) || category == null){
            throw new ApiException(INVALID_INPUT, REQUIRED_FIELDS_MUST_BE_PROVIDED.getMessage());
        }
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        if (itemRepository.existsByCodeAndBrandId(itemCode, brand.getId())) throw new ApiException(DUPLICATE_ITEM);
        Item savedItem = itemRepository.save(Item.builder()
                .code(itemCode)
                .koreanName(koreanName)
                .englishName(englishName)
                .mainImage(mainImage)
                .brand(brand)
                .category(category)
                .price(price)
                .build());
        itemImages.stream()
                .map(e -> ItemImage.builder().item(savedItem).url(e).build())
                .forEach(itemImageRepository::save);

        // add 손지민: 실시간 Top10 을 위해 테이블 생성
        ItemViewCount itemViewCount = ItemViewCount.builder()
                .category(savedItem.getCategory())
                .item(savedItem)
                .viewCount(0L)
                .calculatedAt(LocalDateTime.now())
                .build();
        itemViewCountRepository.save(itemViewCount);

        return getItemDetailResponse(savedItem, brand);
    }

    @Transactional(readOnly = true)
    public ItemDetailResponse getItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        increaseViewCount(item);
        return getItemDetailResponse(item, item.getBrand());
    }

    @Transactional
    public void increaseViewCount(Item item) {
        item.increaseViewCount();
        // add 손지민: ItemViewCount 의 viewCount 증가
        Optional<ItemViewCount> itemViewCountOpt = itemViewCountRepository.findByItemAndCategory(item, item.getCategory());
        itemViewCountOpt.ifPresent(ItemViewCount::increaseViewCount);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleResponse> getItem10List(Long userId, Sort sort) {
        LocalDateTime now = LocalDateTime.now();
        return switch (sort) {
            case RELEASE_NOW -> itemRepository.getReleaseTodayItemOrdered(userId, now);
            case RELEASE_WISH -> itemRepository.getReleaseWishItemOrdered(userId, now);
            case RELEASE_CONFIRM -> itemRepository.getJustConfirmReleaseItemOrdered(userId, now);
            case REGISTER_TODAY -> itemRepository.getRegisterTodayItemOrdered(userId, now);
            default -> itemRepository.getDueTodayItemOrdered(userId, now);
        };
    }

    @Transactional
    public ItemDetailResponse updateItem(Long itemId,
                                         String koreanName, String englishName, String itemCode, String mainImage, List<String> itemImages, String brandName, Category category, Integer price) {
        if (koreanName == null && englishName == null && itemCode == null && itemImages == null && brandName == null && category == null && price == null) {
            throw new ApiException(INVALID_INPUT);
        }
        Item origin = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        Brand brand = null;
        if (itemCode == null && StringUtils.hasText(brandName)) {
            brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            if (itemRepository.existsByCodeAndBrandId(origin.getCode(), brand.getId())) {
                throw new ApiException(DUPLICATE_ITEM);
            }
            origin.updateBrand(brand);
        } else if (StringUtils.hasText(itemCode) && brandName == null) {
            if (itemRepository.existsByCodeAndBrandId(itemCode, origin.getBrand().getId())) {
                throw new ApiException(DUPLICATE_ITEM);
            }
            origin.updateCode(itemCode);
        } else if (StringUtils.hasText(brandName) && StringUtils.hasText(itemCode)) {
            brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            if (itemRepository.existsByCodeAndBrandId(itemCode, brand.getId())) {
                throw new ApiException(DUPLICATE_ITEM);
            }
            origin.updateBrand(brand);
            origin.updateCode(itemCode);
        }
        if (StringUtils.hasText(mainImage)) {
            origin.updateMainImage(mainImage);
        }
        if (itemImages != null && !itemImages.isEmpty()) {
            itemImageRepository.deleteAllByItemId(origin.getId());
            itemImages.stream()
                    .map(e -> ItemImage.builder().item(origin).url(e).build())
                    .forEach(itemImageRepository::save);
        }
        if (category != null) {
            origin.updateCategory(category);
        }
        if (StringUtils.hasText(koreanName)) {
            origin.updateKoreanName(koreanName);
        }
        if (StringUtils.hasText(englishName)) {
            origin.updateEnglishName(englishName);
        }
        if (price != null) {
            origin.updatePrice(price);
        }
        return getItemDetailResponse(origin, brand == null ? origin.getBrand() : brand);
    }

    @Transactional
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        itemRepository.delete(item);
    }

    private ItemDetailResponse getItemDetailResponse(Item item, Brand brand) {
        return new ItemDetailResponse(
                item.getId(),
                item.getKoreanName(),
                item.getEnglishName(),
                item.getCode(),
                item.getMainImage(),
                itemImageRepository.findAllByItemId(item.getId()).stream().map(ItemImage::getUrl).toList(),
                brand.getId(),
                brand.getKoreanName(),
                brand.getEnglishName(),
                item.getViewCount(),
                item.getCategory().getDesc(),
                item.getPrice());
    }
}
