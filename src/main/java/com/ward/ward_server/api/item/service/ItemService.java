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
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.Object.enums.Section;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ward.ward_server.global.Object.Constants.API_PAGE_SIZE;
import static com.ward.ward_server.global.exception.ExceptionCode.*;
import static com.ward.ward_server.global.response.error.ErrorMessage.NAME_MUST_BE_PROVIDED;
import static com.ward.ward_server.global.response.error.ErrorMessage.REQUIRED_FIELDS_MUST_BE_PROVIDED;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final BrandRepository brandRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemViewCountRepository itemViewCountRepository;

    @Transactional
    public ItemDetailResponse createItem(String itemCode, String koreanName, String englishName, String mainImage, List<String> itemImages, Long brandId, Category category, Integer price) throws ApiException {
        if (!StringUtils.hasText(koreanName) && !StringUtils.hasText(englishName)) {
            throw new ApiException(INVALID_INPUT, NAME_MUST_BE_PROVIDED.getMessage());
        }
        ValidationUtils.validationNames(koreanName, englishName);
        if (!StringUtils.hasText(itemCode) || brandId == null || category == null) {
            throw new ApiException(INVALID_INPUT, REQUIRED_FIELDS_MUST_BE_PROVIDED.getMessage());
        }
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
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
                .map(e -> ItemImage.builder().itemId(savedItem.getId()).url(e).build())
                .forEach(itemImageRepository::save);

        // add 손지민: 실시간 Top10 을 위해 ItemViewCount 테이블 생성
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
    public List<ItemSimpleResponse> getItem10List(Long userId, Section section, Category category) {
        //HACK DB 시간 설정 전까지는 -9시간으로 비교해야 한다.
        return itemRepository.getHomeSortList(userId, LocalDateTime.now().minusHours(9), category, section);
    }

    @Transactional(readOnly = true)
    public PageResponse<ItemSimpleResponse> getItemPage(Long userId, Section section, Category category, int page) {
        //HACK DB 시간 설정 전까지는 -9시간으로 비교해야 한다.
        Page<ItemSimpleResponse> itemPageInfo = itemRepository.getHomeSortPage(userId, LocalDateTime.now().minusHours(9), category, section, PageRequest.of(page, API_PAGE_SIZE));
        return new PageResponse<>(itemPageInfo.getContent(), itemPageInfo);
    }

    @Transactional
    public ItemDetailResponse updateItem(Long itemId,
                                         String koreanName, String englishName, String itemCode, String mainImage, List<String> itemImages, Long brandId, Category category, Integer price) {
        if (koreanName == null && englishName == null && itemCode == null && itemImages == null && brandId == null && category == null && price == null) {
            throw new ApiException(INVALID_INPUT);
        }
        Item origin = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        Brand brand = null;
        if (itemCode == null && brandId != null) {
            brand = brandRepository.findById(brandId).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            if (itemRepository.existsByCodeAndBrandId(origin.getCode(), brand.getId())) {
                throw new ApiException(DUPLICATE_ITEM);
            }
            origin.updateBrand(brand);
        } else if (StringUtils.hasText(itemCode) && brandId == null) {
            if (itemRepository.existsByCodeAndBrandId(itemCode, origin.getBrand().getId())) {
                throw new ApiException(DUPLICATE_ITEM);
            }
            origin.updateCode(itemCode);
        } else if (brandId != null && StringUtils.hasText(itemCode)) {
            brand = brandRepository.findById(brandId).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
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
                    .map(e -> ItemImage.builder().itemId(origin.getId()).url(e).build())
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
    public void deleteItem(long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ApiException(ITEM_NOT_FOUND);
        }
        itemRepository.deleteById(itemId);
    }

    private ItemDetailResponse getItemDetailResponse(Item item, Brand brand) {
        return new ItemDetailResponse(
                item.getId(),
                item.getKoreanName(),
                item.getEnglishName(),
                item.getCode(),
                item.getMainImage(),
                itemImageRepository.findAllByItemId(item.getId()).stream().map(ItemImage::getUrl).toList(),
                item.getViewCount(),
                item.getCategory().getDesc(),
                item.getPrice(),
                brand.getId(),
                brand.getLogoImage(),
                brand.getKoreanName(),
                brand.getEnglishName());
    }
}
