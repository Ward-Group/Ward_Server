package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.*;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.item.repository.ItemImageRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.wishBrand.WishBrandRepository;
import com.ward.ward_server.api.wishItem.WishItemRepository;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ward.ward_server.global.exception.ExceptionCode.BRAND_NOT_FOUND;
import static com.ward.ward_server.global.exception.ExceptionCode.DUPLICATE_BRAND;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;
    private final ItemRepository itemRepository;
    private final WishItemRepository wishItemRepository;
    private final WishBrandRepository wishBrandRepository;
    private final ItemImageRepository itemImageRepository;

    @Transactional
    public BrandResponse createBrand(String koreanName, String englishName, String brandLogoImage) {
        if ((!StringUtils.hasText(koreanName) && !StringUtils.hasText(englishName)))
            throw new ApiException(ExceptionCode.INVALID_INPUT);
        if (brandRepository.existsByKoreanNameOrEnglishName(koreanName, englishName))
            throw new ApiException(DUPLICATE_BRAND);
        Brand savedBrand = brandRepository.save(Brand.builder()
                .logoImage(brandLogoImage)
                .koreanName(koreanName)
                .englishName(englishName)
                .build());
        return new BrandResponse(savedBrand.getLogoImage(), savedBrand.getKoreanName(), savedBrand.getEnglishName(), savedBrand.getViewCount());
    }

    @Transactional(readOnly = true)
    public Page<BrandInfoResponse> getBrandItemPage(int page, int size) {
        Page<BrandInfoResponse> brandPage = brandRepository.getBrandItemPage(page, size);
//        Map<Long, List<BrandItemResponse>> top3ItemListByBrand = top10brandList.stream()
//                .collect(
//                        Collectors.toMap(
//                                Brand::getId,
//                                brand -> itemRepository.findBrandItemListTop3(brand.getId()).stream()
//                                        .map(item -> new BrandItemResponse(
//                                                item.getName(),
//                                                item.getCode(),
//                                                itemImageRepository.findFirstByItemId(item.getId()).get().getUrl(),
//                                                item.getViewCount(),
//                                                wishItemRepository.countAllByItemId(item.getId())))
//                                        .toList()
//                        ));
//        return top10brandList.stream()
//                .map(brand -> new BrandInfoResponse(
//                        brand.getLogoImage(),
//                        brand.getName(),
//                        brand.getViewCount(),
//                        wishBrandRepository.countAllByBrandId(brand.getId()),
//                        top3ItemListByBrand.get(brand.getId())))
//                .toList();
        return brandPage;
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
            if (StringUtils.hasText(englishName)) brand.updateEnglishName(englishName);
        }
        return new BrandResponse(brand.getLogoImage(), brand.getKoreanName(), brand.getEnglishName(), brand.getViewCount());
    }

    @Transactional
    public void deleteBrand(String brandName) {
        if (!brandRepository.existsByName(brandName)) throw new ApiException(BRAND_NOT_FOUND);
        brandRepository.deleteByName(brandName);
    }

    @Transactional
    public int increaseBrandViewCount(String brandName) {
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        brand.increaseViewCount();
        return brand.getViewCount();
    }
}
