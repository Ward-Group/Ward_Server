package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.ItemCreateRequest;
import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.dto.ItemUpdateRequest;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.ItemImage;
import com.ward.ward_server.api.item.entity.enumtype.Category;
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
public class ItemService {
    private final ItemRepository itemRepository;
    private final BrandRepository brandRepository;
    private final ItemImageRepository itemImageRepository;

    public ItemDetailResponse createItem(ItemCreateRequest request) throws ApiException {
        if (itemRepository.existsByCode(request.itemCode())) throw new ApiException(DUPLICATE_ITEM_CODE);
        Brand brand= brandRepository.findByName(request.brandName()).orElseThrow(()->new ApiException(BRAND_NOT_FOUND));
        Item savedItem = itemRepository.save(Item.builder()
                .name(request.itemName())
                .code(request.itemCode())
                .brand(brand)
                .category(Category.ofKorean(request.category()))
                .price(request.price()).build());
        List<ItemImage> itemImages = request.itemImages().stream()
                .map(e -> ItemImage.builder().item(savedItem).url(e).build())
                .map(itemImageRepository::save)
                .toList();
        savedItem.addItemImages(itemImages);
        return getItemDetailResponse(savedItem);
    }

    @Transactional(readOnly = true)
    public ItemDetailResponse getItem(String itemCode) {
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(itemCode).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        item.increaseViewCount();
        return getItemDetailResponse(item);
    }
    @Transactional(readOnly = true)
    public PageResponse<ItemSimpleResponse> getItemList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> itemPage = itemRepository.findAllByDeletedAtIsNull(pageable);
        List<Item> contents = itemPage.getContent();
        List<ItemSimpleResponse> responses = contents.stream()
                .map(e -> new ItemSimpleResponse(e.getName(), e.getCode(), e.getItemImages().get(0).getUrl(), e.getBrand().getName()))
                .toList();
        return new PageResponse<>(responses, itemPage);
    }

    @Transactional
    public ItemDetailResponse updateItem(String itemCode, ItemUpdateRequest request) {
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(itemCode).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        if (request.brandName() != null && !request.brandName().isBlank()) {
            Brand brand = brandRepository.findByName(request.brandName()).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
            item.updateBrand(brand);
        }
        if (request.itemCode() != null && !request.itemCode().isBlank()) {
            if (itemRepository.existsByCode(request.itemCode())) throw new ApiException(DUPLICATE_ITEM_CODE);
            item.updateCode(request.itemCode());
        }
        if (request.category() != null && !request.category().isBlank())
            item.updateCategory(Category.ofKorean(request.category()));
        if (request.itemName() != null && !request.itemName().isBlank())
            item.updateName(request.itemName());
        if (request.price() != 0) item.updatePrice(request.price());
        return getItemDetailResponse(item);
    }

    @Transactional
    public void deleteItem(String itemCode) {
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(itemCode).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        item.setDeletedAt(LocalDateTime.now());
    }

    private ItemDetailResponse getItemDetailResponse(Item item) {
        return new ItemDetailResponse(item.getName(),
                item.getCode(),
                item.getItemImages().stream().map(ItemImage::getUrl).toList(),
                item.getBrand().getName(),
                item.getViewCount(),
                item.getCategory().getKorean(),
                item.getPrice());
    }
}
