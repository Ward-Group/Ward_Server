package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.BrandRecommendedResponse;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;
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
        assertEquals("브랜드2", result.get(1).koreanName());
        assertEquals("https://example.com/logo2.png", result.get(1).logoImage());
    }
}
