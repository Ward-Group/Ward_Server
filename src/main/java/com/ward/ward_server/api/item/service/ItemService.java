package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.ItemImage;
import com.ward.ward_server.api.item.entity.enumtype.Category;
import com.ward.ward_server.api.item.entity.enumtype.ItemSort;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.item.repository.ItemImageRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final BrandRepository brandRepository;
    private final ItemImageRepository itemImageRepository;

    @Transactional
    public ItemDetailResponse createItem(String itemCode, String koreanName, String englishName, String mainImage, List<String> itemImages, String brandName, String category, Integer price) throws ApiException {
        if ((!StringUtils.hasText(koreanName) && !StringUtils.hasText(englishName)) || !StringUtils.hasText(itemCode) || !StringUtils.hasText(brandName) || !StringUtils.hasText(category))
            throw new ApiException(INVALID_INPUT);
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        if (itemRepository.existsByCodeAndBrandId(itemCode, brand.getId())) throw new ApiException(DUPLICATE_ITEM);
        Item savedItem = itemRepository.save(Item.builder()
                .code(itemCode)
                .koreanName(koreanName)
                .englishName(englishName)
                .mainImage(mainImage)
                .brand(brand)
                .category(Category.ofText(category))
                .price(price)
                .build());
        itemImages.stream()
                .map(e -> ItemImage.builder().item(savedItem).url(e).build())
                .forEach(itemImageRepository::save);
        return getItemDetailResponse(savedItem);
    }

    @Transactional(readOnly = true)
    public ItemDetailResponse getItem(String itemCode, String brandName) {
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Item item = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(itemCode, brand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        item.increaseViewCount();
        return getItemDetailResponse(item);
    }

    @Transactional(readOnly = true)
    public List<ItemSimpleResponse> getItem10List(Long userId, ItemSort sort) {
        log.debug("item sort:{}", sort.toString());
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
    public ItemDetailResponse updateItem(String originItemCode, String originBrandName,
                                         String koreanName, String englishName, String itemCode, String mainImage, List<String> itemImages, String brandName, String category, Integer price) {
        if (koreanName == null && englishName == null && itemCode == null && itemImages == null && brandName == null && category == null && price == null)
            throw new ApiException(INVALID_INPUT);
        Brand originBrand = brandRepository.findByName(originBrandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Item originItem = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(originItemCode, originBrand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        if (itemCode == null && StringUtils.hasText(brandName)) {
            Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            if (itemRepository.existsByCodeAndBrandId(originItemCode, brand.getId()))
                throw new ApiException(DUPLICATE_ITEM);
            originItem.updateBrand(brand);
        } else if (brandName == null && StringUtils.hasText(itemCode)) {
            if (itemRepository.existsByCodeAndBrandId(itemCode, originBrand.getId()))
                throw new ApiException(DUPLICATE_ITEM);
            originItem.updateCode(itemCode);
        } else if (StringUtils.hasText(brandName) && StringUtils.hasText(itemCode)) {
            Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            if (itemRepository.existsByCodeAndBrandId(itemCode, brand.getId())) throw new ApiException(DUPLICATE_ITEM);
            originItem.updateBrand(brand);
            originItem.updateCode(itemCode);
        }
        if (StringUtils.hasText(mainImage)) originItem.updateMainImage(mainImage);
        if (itemImages != null && !itemImages.isEmpty()) {
            itemImageRepository.deleteAllByItemId(originItem.getId());
            itemImages.stream()
                    .map(e -> ItemImage.builder().item(originItem).url(e).build())
                    .forEach(itemImageRepository::save);
        }
        if (StringUtils.hasText(category)) originItem.updateCategory(Category.ofText(category));
        if (StringUtils.hasText(koreanName)) originItem.updateKoreanName(koreanName);
        if (StringUtils.hasText(englishName)) originItem.updateEnglishName(englishName);
        if (price != null) originItem.updatePrice(price);
        return getItemDetailResponse(originItem);
    }

    @Transactional
    public void deleteItem(String itemCode, String brandName) {
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Item item = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(itemCode, brand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        item.setDeletedAt(LocalDateTime.now());
    }

    private ItemDetailResponse getItemDetailResponse(Item item) {
        return new ItemDetailResponse(
                item.getKoreanName(),
                item.getEnglishName(),
                item.getCode(),
                itemImageRepository.findAllByItemId(item.getId()).stream().map(ItemImage::getUrl).toList(),
                item.getBrand().getKoreanName(),
                item.getBrand().getEnglishName(),
                item.getViewCount(),
                item.getCategory().getDesc(),
                item.getPrice());
    }
}
