package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.BrandInfoResponse;
import com.ward.ward_server.api.item.dto.BrandRecommendedResponse;
import com.ward.ward_server.api.item.dto.BrandResponse;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.repository.BrandRepository;
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

import static com.ward.ward_server.global.Object.Constants.HOME_PAGE_SIZE;
import static com.ward.ward_server.global.exception.ExceptionCode.BRAND_NOT_FOUND;
import static com.ward.ward_server.global.exception.ExceptionCode.DUPLICATE_BRAND;
import static com.ward.ward_server.global.response.error.ErrorCode.REQUIRED_FIELDS_MUST_BE_PROVIDED;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    @Transactional
    public BrandResponse createBrand(String koreanName, String englishName, String brandLogoImage) {
        if ((!StringUtils.hasText(koreanName) && !StringUtils.hasText(englishName)) || (englishName != null && !ValidationUtils.isValidEnglish(englishName)))
            throw new ApiException(ExceptionCode.INVALID_INPUT, REQUIRED_FIELDS_MUST_BE_PROVIDED.getMessage());
        if (brandRepository.existsByKoreanNameOrEnglishName(koreanName, englishName))
            throw new ApiException(DUPLICATE_BRAND);
        Brand savedBrand = brandRepository.save(Brand.builder()
                .logoImage(brandLogoImage)
                .koreanName(koreanName)
                .englishName(englishName)
                .build());
        return new BrandResponse(savedBrand.getId(), savedBrand.getLogoImage(), savedBrand.getKoreanName(), savedBrand.getEnglishName(), savedBrand.getViewCount());
    }

    @Transactional(readOnly = true)
    public Page<BrandInfoResponse> getBrandItemPage(int page) {
        return brandRepository.getBrandItemPage(PageRequest.of(page, HOME_PAGE_SIZE));
    }

    @Transactional(readOnly = true)
    public List<BrandRecommendedResponse> getRecommendedBrands() {
        return brandRepository.findTop10ByOrderByViewCountDesc()
                .stream()
                .map(brand -> new BrandRecommendedResponse(brand.getLogoImage(), brand.getKoreanName(), brand.getEnglishName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public BrandResponse updateBrand(String originBrandName, String koreanName, String englishName, String brandLogoImage) {
        if (koreanName == null && englishName == null && brandLogoImage == null)
            throw new ApiException(ExceptionCode.INVALID_INPUT);
        Brand brand = brandRepository.findByName(originBrandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));

        if (StringUtils.hasText(brandLogoImage)) brand.updateLogoImage(brandLogoImage);
        if (StringUtils.hasText(koreanName) || StringUtils.hasText(englishName)) {
            if (brandRepository.existsByKoreanNameOrEnglishName(koreanName, englishName))
                throw new ApiException(DUPLICATE_BRAND);
            if (StringUtils.hasText(koreanName)) brand.updateKoreanName(koreanName);
            if (StringUtils.hasText(englishName)) {
                if (ValidationUtils.isValidEnglish(englishName)) brand.updateEnglishName(englishName);
                else throw new ApiException(ExceptionCode.INVALID_INPUT);
            }
        }
        return new BrandResponse(brand.getLogoImage(), brand.getKoreanName(), brand.getEnglishName(), brand.getViewCount());
    }

    @Transactional
    public void deleteBrand(String brandName) {
        if (!brandRepository.existsByName(brandName)) throw new ApiException(BRAND_NOT_FOUND);
        brandRepository.deleteByName(brandName);
    }

    @Transactional
    public long increaseBrandViewCount(String brandName) {
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        brand.increaseViewCount();
        return brand.getViewCount();
    }
}
