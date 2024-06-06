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
import com.ward.ward_server.global.Object.PageResponse;
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
    public ItemDetailResponse createItem(String koreanName, String englishName, String itemCode, List<String> itemImages, String brandName, String category, Integer price) throws ApiException {
        if (!StringUtils.hasText(koreanName))
            throw new ApiException(INVALID_INPUT);
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        if (itemRepository.existsByCodeAndBrandId(itemCode, brand.getId()))
            throw new ApiException(DUPLICATE_ITEM);
        Item savedItem = itemRepository.save(Item.builder()
                .koreanName(koreanName)
                .code(itemCode)
                .brand(brand)
                .category(Category.ofKorean(category))
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
        log.info("item sort:{}", sort.toString());
        LocalDateTime now = LocalDateTime.now();
        return switch (sort) {
            case RELEASE_TODAY -> itemRepository.getReleaseTodayItem10Ordered(userId, now);
            case WISH_RELEASE -> itemRepository.getReleaseWishItem10Ordered(userId, now);
            case CONFIRM_RELEASE -> itemRepository.getNotReleaseItem10Ordered(userId, now);
            case REGISTER_TODAY -> itemRepository.getRegisterTodayItem10Ordered(userId, now);
            default -> itemRepository.getDueTodayItem10Ordered(userId, now);
        };
    }

    public PageResponse<ItemSimpleResponse> getItemPageOrdered(int page, int size) {
        return null;
    }

    @Transactional
    public ItemDetailResponse updateItem(String originItemCode, String originBrandName,
                                         String itemName, String itemCode, List<String> itemImages, String brandName, String category, Integer price) {
        if (itemName == null && itemCode == null && itemImages == null && brandName == null && category == null && price == null)
            throw new ApiException(INVALID_INPUT);
        Brand originBrand = brandRepository.findByName(originBrandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        Item originItem = itemRepository.findByCodeAndBrandIdAndDeletedAtIsNull(originItemCode, originBrand.getId()).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        if (itemCode == null && brandName != null && !brandName.isBlank()) {
            Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            if (itemRepository.existsByCodeAndBrandId(originItemCode, brand.getId()))
                throw new ApiException(DUPLICATE_ITEM);
            originItem.updateBrand(brand);
        } else if (brandName == null && itemCode != null && !itemCode.isBlank()) {
            if (itemRepository.existsByCodeAndBrandId(itemCode, originBrand.getId()))
                throw new ApiException(DUPLICATE_ITEM);
            originItem.updateCode(itemCode);
        } else if (brandName != null && !brandName.isBlank() && !itemCode.isBlank()) {
            Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            if (itemRepository.existsByCodeAndBrandId(itemCode, brand.getId()))
                throw new ApiException(DUPLICATE_ITEM);
            originItem.updateBrand(brand);
            originItem.updateCode(itemCode);
        }
        if (itemImages != null && !itemImages.isEmpty()) {
            itemImageRepository.deleteAllByItemId(originItem.getId());
            itemImages.stream()
                    .map(e -> ItemImage.builder().item(originItem).url(e).build())
                    .forEach(itemImageRepository::save);
        }
        if (category != null && !category.isBlank())
            originItem.updateCategory(Category.ofKorean(category));
        if (itemName != null && !itemName.isBlank())
            originItem.updateName(itemName);
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
        return new ItemDetailResponse(item.getName(),
                item.getCode(),
                itemImageRepository.findAllByItemId(item.getId()).stream().map(ItemImage::getUrl).toList(),
                item.getBrand().getName(),
                item.getViewCount(),
                item.getCategory().getKorean(),
                item.getPrice());
    }
}
