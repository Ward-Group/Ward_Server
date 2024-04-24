package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.ItemCreateRequest;
import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.dto.ItemUpdateRequest;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.ItemImage;
import com.ward.ward_server.api.item.entity.enumtype.Brand;
import com.ward.ward_server.api.item.entity.enumtype.Category;
import com.ward.ward_server.api.item.repository.ItemImageRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.global.entity.PageResponse;
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

import static com.ward.ward_server.global.exception.ExceptionCode.DUPLICATE_ITEM_CODE;
import static com.ward.ward_server.global.exception.ExceptionCode.ITEM_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;

    public ItemDetailResponse createItem(ItemCreateRequest itemCreateRequest) throws ApiException {
        if (itemRepository.existsByCode(itemCreateRequest.code())) throw new ApiException(DUPLICATE_ITEM_CODE);
        Item savedItem = itemRepository.save(Item.builder()
                .name(itemCreateRequest.name())
                .code(itemCreateRequest.code())
                .brand(Brand.ofKorean(itemCreateRequest.brand()))
                .category(Category.ofKorean(itemCreateRequest.category()))
                .price(itemCreateRequest.price()).build());
        List<ItemImage> itemImages = itemCreateRequest.imageUrls().stream()
                .map(e -> ItemImage.builder().item(savedItem).url(e).build())
                .map(itemImageRepository::save)
                .toList();
        savedItem.addItemImages(itemImages);
        return getItemDetailResponse(savedItem);
    }

    @Transactional
    public ItemDetailResponse getItem(String itemCode) {
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(itemCode).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        item.increaseViewCount();
        return getItemDetailResponse(item);
    }

    public PageResponse<ItemSimpleResponse> getItemList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> itemPage = itemRepository.findAllByDeletedAtIsNull(pageable);
        List<Item> contents = itemPage.getContent();
        List<ItemSimpleResponse> responses = contents.stream()
                .map(e -> new ItemSimpleResponse(e.getName(), e.getCode(), e.getItemImages().get(0).toString(), e.getBrand().getKorean()))
                .toList();
        return new PageResponse<>(responses, itemPage);
    }

    @Transactional
    public ItemDetailResponse updateItem(String itemCode, ItemUpdateRequest itemUpdateRequest) {
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(itemCode).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        if(itemUpdateRequest.brand()!=null && !itemUpdateRequest.brand().isBlank()) item.updateBrand(Brand.ofKorean(itemUpdateRequest.brand()));
        if(itemUpdateRequest.code()!=null && !itemUpdateRequest.code().isBlank()) {
            if (itemRepository.existsByCode(itemUpdateRequest.code())) throw new ApiException(DUPLICATE_ITEM_CODE);
            item.updateCode(itemUpdateRequest.code());
        }
        if(itemUpdateRequest.category()!=null && !itemUpdateRequest.category().isBlank()) item.updateCategory(Category.ofKorean(itemUpdateRequest.category()));
        if(itemUpdateRequest.name()!=null && !itemUpdateRequest.name().isBlank()) item.updateName(itemUpdateRequest.name());
        if(itemUpdateRequest.price()!=0) item.updatePrice(itemUpdateRequest.price());
        return getItemDetailResponse(item);
    }

    @Transactional
    public void deleteItem(String itemCode){
        Item item = itemRepository.findByCodeAndDeletedAtIsNull(itemCode).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        item.setDeletedAt(LocalDateTime.now());
    }

    private ItemDetailResponse getItemDetailResponse(Item item){
        return new ItemDetailResponse(item.getName(),
                item.getCode(),
                item.getItemImages().stream().map(ItemImage::getUrl).toList(),
                item.getBrand().getKorean(),
                item.getViewCount(),
                item.getCategory().getKorean(),
                item.getPrice());
    }
}
