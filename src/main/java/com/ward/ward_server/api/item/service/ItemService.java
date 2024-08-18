package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.dto.ItemTopRankResponse;
import com.ward.ward_server.api.item.entity.*;
import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.api.item.repository.*;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.Object.enums.Section;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.util.S3ImageManager;
import com.ward.ward_server.global.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.ward.ward_server.global.Object.Constants.API_PAGE_SIZE;
import static com.ward.ward_server.global.exception.ExceptionCode.*;
import static com.ward.ward_server.global.response.error.ErrorMessage.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final BrandRepository brandRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemViewCountRepository itemViewCountRepository;
    private final ItemTopRankRepository itemTopRankRepository;
    private final S3ImageManager imageManager;
    private final String MAIN_IMAGE_DIR_NAME = "item/main";
    private final String SUB_IMAGES_DIR_NAME = "item/sub";

    @Transactional
    public ItemDetailResponse createItem(String itemCode, String koreanName, String englishName, Long brandId, Category category, Integer price,
                                         MultipartFile mainImage, List<MultipartFile> itemImages) throws ApiException, IOException {
        if (!StringUtils.hasText(koreanName) && !StringUtils.hasText(englishName)) {
            throw new ApiException(INVALID_INPUT, NAME_MUST_BE_PROVIDED.getMessage());
        }
        ValidationUtils.validationNames(koreanName, englishName);
        if (!StringUtils.hasText(itemCode) || brandId == null || category == null) {
            throw new ApiException(INVALID_INPUT, REQUIRED_FIELDS_MUST_BE_PROVIDED.getMessage());
        }

        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        if (itemRepository.existsByCodeAndBrandId(itemCode, brand.getId())) {
            throw new ApiException(DUPLICATE_ITEM);
        }
        String uploadedMainImageUrl = mainImage != null ? imageManager.upload(mainImage, MAIN_IMAGE_DIR_NAME) : null;
        Item savedItem = itemRepository.save(Item.builder()
                .code(itemCode)
                .koreanName(koreanName)
                .englishName(englishName)
                .mainImage(uploadedMainImageUrl)
                .brand(brand)
                .category(category)
                .price(price)
                .build());
        if (itemImages != null) {
            itemImages.stream()
                    .map(e -> {
                        try {
                            return ItemImage.builder()
                                    .itemId(savedItem.getId())
                                    .url(imageManager.upload(e, SUB_IMAGES_DIR_NAME))
                                    .build();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    })
                    .forEach(itemImageRepository::save);
        }
        // add 손지민: 실시간 Top10 을 위해 ItemViewCount 테이블 생성
        ItemViewCount itemViewCount = ItemViewCount.builder()
                .category(savedItem.getCategory())
                .item(savedItem)
                .calculatedAt(LocalDateTime.now())
                .build();
        itemViewCountRepository.save(itemViewCount);

        return getItemDetailResponse(savedItem, brand);
    }

    @Transactional
    public ItemDetailResponse getItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        increaseViewCount(item);
        return getItemDetailResponse(item, item.getBrand());
    }

    @Transactional
    public void increaseViewCount(Item item) {
        item.increaseViewCount();
        itemViewCountRepository.save(ItemViewCount.builder()
                .category(item.getCategory())
                .item(item)
                .calculatedAt(LocalDateTime.now())
                .build());
    }

    public List<ItemSimpleResponse> getItem10List(Long userId, Section section, Category category) {
        return switch (section) {
            case DUE_TODAY, RELEASE_NOW, RELEASE_SCHEDULE ->
                    itemRepository.getItem10List(userId, LocalDateTime.now().minusHours(9), category, section); //HACK DB 시간 설정 전까지는 -9시간으로 비교해야 한다.
            default -> throw new ApiException(INVALID_INPUT, SECTION_NOT_AVAILABLE_THIS_PAGE.getMessage());
        };
    }

    public PageResponse<ItemSimpleResponse> getItemPage(Long userId, Section section, Category category, int page, String date) {
        return switch (section) {
            case RELEASE_SCHEDULE, CLOSED -> {
                Page<ItemSimpleResponse> itemPageInfo = itemRepository.getItemPage(userId, LocalDateTime.now().minusHours(9), category, section, date, PageRequest.of(page, API_PAGE_SIZE)); //HACK DB 시간 설정 전까지는 -9시간으로 비교해야 한다.
                yield new PageResponse<>(itemPageInfo.getContent(), itemPageInfo);
            }
            default -> throw new ApiException(INVALID_INPUT, SECTION_NOT_AVAILABLE_THIS_PAGE.getMessage());
        };
    }

    public List<ItemTopRankResponse> getTopItemsResponseByCategory(Category category, int limit) {
        List<ItemTopRank> topItems;
        if (category == Category.ALL) {
            topItems = itemTopRankRepository.findTopItems(PageRequest.of(0, limit));
        } else {
            topItems = itemTopRankRepository.findTopItemsByCategory(category, PageRequest.of(0, limit));
        }
        return convertToTopResponse(topItems);
    }

    private List<ItemTopRankResponse> convertToTopResponse(List<ItemTopRank> topItems) {
        return topItems.stream()
                .map(itemTopRank -> new ItemTopRankResponse(
                        itemTopRank.getItemRank(),
                        itemTopRank.getItem().getId(),
                        itemTopRank.getItem().getMainImage(),
                        itemTopRank.getItem().getBrand().getKoreanName(),
                        itemTopRank.getItem().getKoreanName()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemDetailResponse updateItem(Long itemId,
                                         String koreanName, String englishName, String itemCode, Long brandId, Category category, Integer price,
                                         MultipartFile mainImage, List<MultipartFile> itemImages) throws IOException {
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
        if (mainImage != null) {
            imageManager.delete(origin.getMainImage());
            String uploadedImageUrl = imageManager.upload(mainImage, MAIN_IMAGE_DIR_NAME);
            origin.updateMainImage(uploadedImageUrl);
        }
        if (itemImages != null && !itemImages.isEmpty()) {
            //TODO 기존 이미지를 삭제하는 로직 혹은 api 추가(지민님과 의논)
            itemImages.stream()
                    .map(e -> {
                        try {
                            return ItemImage.builder()
                                    .itemId(itemId)
                                    .url(imageManager.upload(e, SUB_IMAGES_DIR_NAME))
                                    .build();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    })
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
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        imageManager.delete(item.getMainImage());
        itemImageRepository.findAllByItemId(itemId)
                .forEach(i -> imageManager.delete(i.getUrl()));
        itemRepository.delete(item);
    }

    private ItemDetailResponse getItemDetailResponse(Item item, Brand brand) {
        return new ItemDetailResponse(
                item.getId(),
                item.getKoreanName(),
                item.getEnglishName(),
                item.getCode(),
                item.getMainImage() == null ?
                        imageManager.getUrl(MAIN_IMAGE_DIR_NAME + "/item-basic-main-image.png") : item.getMainImage(),
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
