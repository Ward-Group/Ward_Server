package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.BrandRecommendedResponse;
import com.ward.ward_server.api.item.dto.BrandResponse;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.global.util.S3ImageManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {
    @Mock
    private BrandRepository brandRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ReleaseInfoRepository releaseInfoRepository;
    @Mock
    private S3ImageManager s3ImageManager;
    @InjectMocks
    private BrandService brandService;

    @Test
    void testGetRecommendedBrands() {
        List<Brand> mockBrands = List.of(
                Brand.builder().logoImage("https://example.com/logo1.png").koreanName("브랜드1").englishName("Brand1").build(),
                Brand.builder().logoImage("https://example.com/logo2.png").koreanName("브랜드2").englishName("Brand2").build()
        );

        when(brandRepository.findTop10ByOrderByViewCountDesc()).thenReturn(mockBrands);

        List<BrandRecommendedResponse> result = brandService.getRecommendedBrands();

        assertEquals(2, result.size());
        assertEquals("브랜드1", result.get(0).koreanName());
        assertEquals("https://example.com/logo1.png", result.get(0).logoImage());
        assertEquals("Brand1", result.get(0).englishName());
        assertEquals("브랜드2", result.get(1).koreanName());
        assertEquals("https://example.com/logo2.png", result.get(1).logoImage());
        assertEquals("Brand2", result.get(1).englishName());
    }

    @Test
    void 브랜드_생성시_로고_이미지를_입력하지_않으면_기본_이미지로_출력한다() throws IOException {
        //given
        long id = 1L;
        String koreanName = "전 브랜드한글이름";
        String englishName = "before brand englishName";
        Brand mockBrand = Brand.builder()
                .koreanName(koreanName)
                .englishName(englishName)
                .logoImage(null)
                .build();
        ReflectionTestUtils.setField(mockBrand, "id", id);
        when(brandRepository.save(any())).thenReturn(mockBrand);

        when(brandRepository.existsByKoreanNameOrEnglishName(koreanName, englishName)).thenReturn(false);
        String mockBasicLogoImageUrl = "https://mock.cloudfront.net";
        when(s3ImageManager.getUrl(Mockito.anyString())).thenReturn(mockBasicLogoImageUrl);
        //when
        BrandResponse result = brandService.createBrand(koreanName, englishName, null);
        //then
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.koreanName()).isEqualTo(koreanName);
        assertThat(result.englishName()).isEqualTo(englishName);
        assertThat(result.logoImage()).isEqualTo(mockBasicLogoImageUrl);
    }

    @Test
    void 브랜드_수정_로직을_확인한다() throws IOException {
        //given
        long originBrandId = 1L;
        String originBrandKoreanName = "전 브랜드한글이름";
        String originBrandEnglishName = "before brand englishName";
        String originBrandLogoImage = "https://mock-before-brand-logo-image.net";
        Brand mockBrand = Brand.builder()
                .koreanName(originBrandKoreanName)
                .englishName(originBrandEnglishName)
                .logoImage(originBrandLogoImage)
                .build();
        ReflectionTestUtils.setField(mockBrand, "id", originBrandId);
        when(brandRepository.findById(originBrandId)).thenReturn(Optional.of(mockBrand));

        String targetBrandKoreanName = "수정후 브랜드한글이름";
        String targetBrandEnglishName = "after brand englishName";
        MultipartFile targetMultipartFile = new MockMultipartFile(
                "test",
                "mock-logo.png",
                "image/png",
                "test-logo".getBytes()
        );

        String mockBrandLogoImageUrl = "https://mock.cloudfront.net";
        when(s3ImageManager.upload(eq(targetMultipartFile), anyString())).thenReturn(mockBrandLogoImageUrl);

        //when
        BrandResponse result = brandService.updateBrand(originBrandId, targetBrandKoreanName, targetBrandEnglishName, targetMultipartFile);

        //then
        assertThat(result.id()).isEqualTo(originBrandId);
        assertThat(result.koreanName()).isEqualTo(targetBrandKoreanName);
        assertThat(result.englishName()).isEqualTo(targetBrandEnglishName);
        assertThat(result.logoImage()).isEqualTo(mockBrandLogoImageUrl);
    }
}
