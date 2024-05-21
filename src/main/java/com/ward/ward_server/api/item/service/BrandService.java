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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public BrandResponse createBrand(BrandRequest request) {
        if (request.brandName() == null || request.brandName().isBlank() || request.brandLogoImage() == null || request.brandLogoImage().isBlank())
            throw new ApiException(ExceptionCode.INVALID_INPUT);
        if (brandRepository.existsByName(request.brandName())) throw new ApiException(DUPLICATE_BRAND);
        Brand savedBrand = brandRepository.save(Brand.builder()
                .logoImage(request.brandLogoImage())
                .name(request.brandName())
                .build());
        return new BrandResponse(savedBrand.getLogoImage(), savedBrand.getName(), savedBrand.getViewCount());
    }

    //FIXME 쿼리 개선 필요
    @Transactional(readOnly = true)
    public List<TopBrandResponse> getBrandTop10AndItem3List() {
        List<Brand> top10brandList = brandRepository.findBrandListTop10();
        Map<Long, List<TopBrandItemResponse>> top3ItemListByBrand = top10brandList.stream()
                .collect(
                        Collectors.toMap(
                                Brand::getId,
                                brand -> itemRepository.findBrandItemListTop3(brand.getId()).stream()
                                        .map(item -> new TopBrandItemResponse(
                                                item.getName(),
                                                item.getCode(),
                                                itemImageRepository.findFirstByItemId(item.getId()).get().getUrl(),
                                                item.getViewCount(),
                                                wishItemRepository.countAllByItemId(item.getId())))
                                        .toList()
                        ));
        return top10brandList.stream()
                .map(brand -> new TopBrandResponse(
                        brand.getLogoImage(),
                        brand.getName(),
                        brand.getViewCount(),
                        wishBrandRepository.countAllByBrandId(brand.getId()),
                        top3ItemListByBrand.get(brand.getId())))
                .toList();
    }

    @Transactional
    public BrandResponse updateBrand(String originBrandName, BrandRequest request) {
        if (request.brandName() == null && request.brandLogoImage() == null) throw new ApiException(ExceptionCode.INVALID_INPUT);
        Brand brand = brandRepository.findByName(originBrandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        if (request.brandLogoImage() != null && !request.brandLogoImage().isBlank()) brand.updateLogoImage(request.brandLogoImage());
        if (request.brandName() != null && !request.brandName().isBlank()) {
            if (brandRepository.existsByName(request.brandName())) throw new ApiException(DUPLICATE_BRAND);
            brand.updateName(request.brandName());
        }
        return new BrandResponse(brand.getLogoImage(), brand.getName(), brand.getViewCount());
    }

    @Transactional
    public void deleteBrand(String brandName) {
        if (!brandRepository.existsByName(brandName)) throw new ApiException(BRAND_NOT_FOUND);
        brandRepository.deleteByName(brandName);
    }

    @Transactional
    public void increaseBrandViewCount(String brandName){
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        brand.increaseViewCount();
    }
}
