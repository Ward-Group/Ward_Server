package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.api.item.repository.*;
import com.ward.ward_server.global.Object.enums.Section;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.util.S3ImageManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static com.ward.ward_server.global.exception.ExceptionCode.INVALID_INPUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    BrandRepository brandRepository;
    @Mock
    ItemImageRepository itemImageRepository;
    @Mock
    ItemViewCountRepository itemViewCountRepository;
    @Mock
    ItemTopRankRepository itemTopRankRepository;
    @Mock
    S3ImageManager s3ImageManager;
    @InjectMocks
    ItemService itemService;

    @Test
    void 제공하지_않는_섹션으로_접근시_예외를_발생한다_10List() {
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> itemService.getItem10List(1L, Section.CLOSED, Category.FOOTWEAR))
                .withMessage(INVALID_INPUT.getMessage());
    }

    @Test
    void 제공하지_않는_섹션으로_접근시_예외를_발생한다_page() {
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> itemService.getItemPage(1L, Section.REGISTER_TODAY, Category.FOOTWEAR, 1, "2024-07"))
                .withMessage(INVALID_INPUT.getMessage());
    }

    @Test
    void 상품_생성시_메인_이미지를_입력하지_않으면_기본_이미지로_출력한다() throws IOException {
        //given
        long itemId = 1L;
        String itemCode = "상품코드";
        String itemKoreanName = "상품한글이름";
        String itemEnglishName = "itemEnglishName";
        Category category = Category.FOOTWEAR;
        int price = 10000;
        Item mockItem = Item.builder()
                .code(itemCode)
                .koreanName(itemKoreanName)
                .englishName(itemEnglishName)
                .category(category)
                .price(price)
                .build();
        ReflectionTestUtils.setField(mockItem, "id", itemId);
        when(itemRepository.save(Mockito.any())).thenReturn(mockItem);

        long brandId = 1L;
        String brandKoreanName = "브랜드한글이름";
        String brandEnglishName = "brandEnglishName";
        String brandLogoImage = "https://mock-brand-logo-image.net";
        Brand mockBrand = Brand.builder()
                .koreanName(brandKoreanName)
                .englishName(brandEnglishName)
                .logoImage(brandLogoImage)
                .build();
        ReflectionTestUtils.setField(mockBrand, "id", brandId);
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(mockBrand));

        String mockBasicMainImageUrl = "https://mock.cloudfront.net";
        when(s3ImageManager.getUrl(Mockito.anyString())).thenReturn(mockBasicMainImageUrl);

        when(itemRepository.existsByCodeAndBrandId(itemCode, brandId)).thenReturn(false);

        //when
        ItemDetailResponse result = itemService.createItem(itemCode, itemKoreanName, itemEnglishName, brandId, category, price, null, null);

        //then
        assertThat(result.itemId()).isEqualTo(itemId);
        assertThat(result.itemKoreanName()).isEqualTo(itemKoreanName);
        assertThat(result.itemEnglishName()).isEqualTo(itemEnglishName);
        assertThat(result.itemCode()).isEqualTo(itemCode);
        assertThat(result.mainImage()).isEqualTo(mockBasicMainImageUrl);
        assertThat(result.itemImages()).isEqualTo(new ArrayList<>());
        assertThat(result.viewCount()).isEqualTo(0);
        assertThat(result.category()).isEqualTo(category.getDesc());
        assertThat(result.price()).isEqualTo(price);

        assertThat(result.brandId()).isEqualTo(brandId);
        assertThat(result.brandKoreanName()).isEqualTo(brandKoreanName);
        assertThat(result.brandEnglishName()).isEqualTo(brandEnglishName);
        assertThat(result.brandLogoImage()).isEqualTo(brandLogoImage);
    }

    @Test
    void 이미지를_제외한_상품_수정_로직을_확인한다() throws IOException {
        //given
        long itemId = 1L;
        String originItemCode = "전 상품코드";
        String originItemKoreanName = "전 상품한글이름";
        String originItemEnglishName = "before item englishName";
        Category originCategory = Category.FOOTWEAR;
        int originPrice = 10000;
        Item mockItem = Item.builder()
                .code(originItemCode)
                .koreanName(originItemKoreanName)
                .englishName(originItemEnglishName)
                .category(originCategory)
                .price(originPrice)
                .build();
        ReflectionTestUtils.setField(mockItem, "id", itemId);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(mockItem));

        long targetBrandId = 2L;
        String targetBrandKoreanName = "수정후 브랜드한글이름";
        String targetBrandEnglishName = "after brand englishName";
        String targetBrandLogoImage = "https://mock-after-brand-logo-image.net";
        Brand mockTargetBrand = Brand.builder()
                .koreanName(targetBrandKoreanName)
                .englishName(targetBrandEnglishName)
                .logoImage(targetBrandLogoImage)
                .build();
        ReflectionTestUtils.setField(mockTargetBrand, "id", targetBrandId);
        when(brandRepository.findById(targetBrandId)).thenReturn(Optional.of(mockTargetBrand));

        when(itemRepository.existsByCodeAndBrandId(Mockito.anyString(), Mockito.anyLong())).thenReturn(false);

        String mockBasicMainImageUrl = "https://mock.cloudfront.net";
        when(s3ImageManager.getUrl(Mockito.anyString())).thenReturn(mockBasicMainImageUrl);

        String targetItemKoreanName = "수정후 상품한글이름";
        String targetItemEnglishName = "after item englishName";
        String targetItemCode = "수정후 상품코드";
        Category targetCategory = Category.ACCESSORY;
        int targetPrice = 20000;

        //when
        ItemDetailResponse result = itemService.updateItem(itemId,
                targetItemKoreanName, targetItemEnglishName, targetItemCode, targetBrandId, targetCategory, targetPrice,
                null, null);

        //then
        assertThat(result.itemId()).isEqualTo(itemId);
        assertThat(result.itemKoreanName()).isEqualTo(targetItemKoreanName);
        assertThat(result.itemEnglishName()).isEqualTo(targetItemEnglishName);
        assertThat(result.itemCode()).isEqualTo(targetItemCode);
        assertThat(result.mainImage()).isEqualTo(mockBasicMainImageUrl);
        assertThat(result.itemImages()).isEqualTo(new ArrayList<>());
        assertThat(result.viewCount()).isEqualTo(0);
        assertThat(result.category()).isEqualTo(targetCategory.getDesc());
        assertThat(result.price()).isEqualTo(targetPrice);

        assertThat(result.brandId()).isEqualTo(targetBrandId);
        assertThat(result.brandKoreanName()).isEqualTo(targetBrandKoreanName);
        assertThat(result.brandEnglishName()).isEqualTo(targetBrandEnglishName);
        assertThat(result.brandLogoImage()).isEqualTo(targetBrandLogoImage);
    }

    @Test
    void 이미지_수정_로직을_확인한다(){
        //TODO
    }
}