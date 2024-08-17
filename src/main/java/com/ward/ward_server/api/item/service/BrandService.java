package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.BrandInfoResponse;
import com.ward.ward_server.api.item.dto.BrandItemResponse;
import com.ward.ward_server.api.item.dto.BrandRecommendedResponse;
import com.ward.ward_server.api.item.dto.BrandResponse;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.Object.enums.BasicSort;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.util.S3ImageManager;
import com.ward.ward_server.global.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.ward.ward_server.global.Object.Constants.API_PAGE_SIZE;
import static com.ward.ward_server.global.exception.ExceptionCode.*;
import static com.ward.ward_server.global.response.error.ErrorMessage.NAME_MUST_BE_PROVIDED;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final ItemRepository itemRepository;
    private final ReleaseInfoRepository releaseInfoRepository;
    private final S3ImageManager imageManager;
    private final String DIR_NAME = "brand/logo";

    @Transactional
    public BrandResponse createBrand(String koreanName, String englishName, MultipartFile brandLogoImage) throws IOException {
        if (!StringUtils.hasText(koreanName) && !StringUtils.hasText(englishName)) {
            throw new ApiException(INVALID_INPUT, NAME_MUST_BE_PROVIDED.getMessage());
        }
        ValidationUtils.validationNames(koreanName, englishName);
        if (brandRepository.existsByKoreanNameOrEnglishName(koreanName, englishName)) {
            throw new ApiException(DUPLICATE_BRAND);
        }
        String uploadedImageUrl = brandLogoImage != null ? imageManager.upload(brandLogoImage, DIR_NAME) : null;
        Brand savedBrand = brandRepository.save(Brand.builder()
                .logoImage(uploadedImageUrl)
                .koreanName(koreanName)
                .englishName(englishName)
                .build());
        return getBrandResponse(savedBrand);
    }

    @Transactional(readOnly = true)
    public PageResponse<BrandInfoResponse> getBrandAndItem3Page(BasicSort sort, int page) {
        Page<BrandInfoResponse> brandInfoPage = brandRepository.getBrandAndItem3Page(sort, PageRequest.of(page, API_PAGE_SIZE));
        return new PageResponse<>(brandInfoPage.getContent(), brandInfoPage);
    }

    @Transactional(readOnly = true)
    public List<BrandRecommendedResponse> getRecommendedBrands() {
        return brandRepository.findTop10ByOrderByViewCountDesc()
                .stream()
                .map(brand -> new BrandRecommendedResponse(brand.getLogoImage(), brand.getKoreanName(), brand.getEnglishName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PageResponse<BrandItemResponse> getBrandItemPage(long brandId, BasicSort sort, int page) {
        Page<BrandItemResponse> brandInfoPage = itemRepository.getBrandItemPage(brandId, sort, PageRequest.of(page, API_PAGE_SIZE));
        return new PageResponse<>(brandInfoPage.getContent(), brandInfoPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<ReleaseInfoSimpleResponse> getBrandReleaseInfoPage(long brandId, int page) {
        Page<ReleaseInfoSimpleResponse> releaseInfoInfoPage = releaseInfoRepository.getBrandReleaseInfoPage(brandId, PageRequest.of(page, API_PAGE_SIZE));
        return new PageResponse<>(releaseInfoInfoPage.getContent(), releaseInfoInfoPage);
    }

    @Transactional
    public BrandResponse updateBrand(long brandId, String koreanName, String englishName, MultipartFile brandLogoImage) throws IOException {
        ValidationUtils.validationNames(koreanName, englishName);
        Brand origin = brandRepository.findById(brandId).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        if (brandLogoImage != null) {
            imageManager.delete(origin.getLogoImage());
            String uploadedLogoImageUrl = imageManager.upload(brandLogoImage, DIR_NAME);
            origin.updateLogoImage(uploadedLogoImageUrl);
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
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        imageManager.delete(brand.getLogoImage());
        brandRepository.delete(brand);
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
                brand.getLogoImage() == null ?
                        imageManager.getUrl(DIR_NAME + "/brand-basic-logo.png") : brand.getLogoImage(),
                brand.getKoreanName(),
                brand.getEnglishName(),
                brand.getViewCount()
        );
    }
}
