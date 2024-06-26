package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.BrandInfoResponse;
import com.ward.ward_server.api.item.dto.BrandRecommendedResponse;
import com.ward.ward_server.api.item.dto.BrandResponse;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.Object.enums.ApiSort;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.ward.ward_server.global.Object.Constants.API_PAGE_SIZE;
import static com.ward.ward_server.global.exception.ExceptionCode.*;
import static com.ward.ward_server.global.response.error.ErrorCode.NAME_MUST_BE_PROVIDED;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    @Transactional
    public BrandResponse createBrand(String koreanName, String englishName, String brandLogoImage) {
        if (!StringUtils.hasText(koreanName) && !StringUtils.hasText(englishName)) {
            throw new ApiException(INVALID_INPUT, NAME_MUST_BE_PROVIDED.getMessage());
        }
        ValidationUtils.validationNames(koreanName, englishName);
        if (brandRepository.existsByKoreanNameOrEnglishName(koreanName, englishName))
            throw new ApiException(DUPLICATE_BRAND);

        Brand savedBrand = brandRepository.save(Brand.builder()
                .logoImage(brandLogoImage)
                .koreanName(koreanName)
                .englishName(englishName)
                .build());
        return getBrandResponse(savedBrand);
    }

    @Transactional(readOnly = true)
    public PageResponse<BrandInfoResponse> getBrandItemPageSortedForHomeView(ApiSort sort, int page) {
        Page<BrandInfoResponse> brandInfoPage = brandRepository.getBrandItemPage(sort, PageRequest.of(page, API_PAGE_SIZE));
        return new PageResponse<>(brandInfoPage.getContent(), brandInfoPage);
    }

    @Transactional(readOnly = true)
    public List<BrandRecommendedResponse> getRecommendedBrands() {
        return brandRepository.findTop10ByOrderByViewCountDesc()
                .stream()
                .map(brand -> new BrandRecommendedResponse(brand.getLogoImage(), brand.getKoreanName(), brand.getEnglishName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public BrandResponse updateBrand(long brandId, String koreanName, String englishName, String brandLogoImage) {
        if (koreanName == null && englishName == null && brandLogoImage == null)
            throw new ApiException(ExceptionCode.INVALID_INPUT);
        ValidationUtils.validationNames(koreanName, englishName);
        Brand origin = brandRepository.findById(brandId).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        if (StringUtils.hasText(brandLogoImage)) {
            origin.updateLogoImage(brandLogoImage);
        }
        if (StringUtils.hasText(koreanName)) {
            origin.updateKoreanName(koreanName);
        }
        if (StringUtils.hasText(englishName)) {
            origin.updateEnglishName(englishName);
        }
        return getBrandResponse(origin);
    }

    @Transactional
    public void deleteBrand(long brandId) {
        if (!brandRepository.existsById(brandId)) {
            throw new ApiException(BRAND_NOT_FOUND);
        }
        brandRepository.deleteById(brandId);
    }

    @Transactional
    public long increaseBrandViewCount(long brandId) {
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        brand.increaseViewCount();
        return brand.getViewCount();
    }

    private BrandResponse getBrandResponse(Brand brand) {
        return new BrandResponse(
                brand.getId(),
                brand.getLogoImage(),
                brand.getKoreanName(),
                brand.getEnglishName(),
                brand.getViewCount()
        );
    }
}
