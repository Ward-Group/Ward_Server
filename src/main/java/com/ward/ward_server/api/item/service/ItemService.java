package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ItemDetailResponse createItem(String itemName, String itemCode, List<String> itemImages, String brandName, String category, Integer price) throws ApiException {
        if (itemName == null || itemName.isBlank() || itemCode == null || itemCode.isBlank() ||
                itemImages == null || itemImages.isEmpty() || brandName == null || brandName.isBlank() ||
                category == null || category.isBlank()) throw new ApiException(INVALID_INPUT);
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        if (itemRepository.existsByCodeAndBrandId(itemCode, brand.getId()))
            throw new ApiException(DUPLICATE_ITEM);
        Item savedItem = itemRepository.save(Item.builder()
                .name(itemName)
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
    public List<ItemSimpleResponse> getItemTop10List(ItemSort sort) {
        log.info("item sort:{}",sort.toString());
        Pageable pageable = PageRequest.of(1,10);
        Page<Item> itemPage = itemRepository.findAllByDeletedAtIsNull(pageable);
        List<Item> contents = itemPage.getContent();
        List<ItemSimpleResponse> responses = contents.stream()
                .map(e -> new ItemSimpleResponse(
                        e.getName(),
                        e.getCode(),
                        itemImageRepository.findAllByItemId(e.getId()).get(0).getUrl(),
                        e.getBrand().getName()))
                .toList();
        return responses;
    }

    public PageResponse<ItemSimpleResponse> getItemPageOrdered(int page, int size){
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
