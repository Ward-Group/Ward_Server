package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.dto.BrandItemResponse;
import com.ward.ward_server.api.item.dto.BrandRequest;
import com.ward.ward_server.api.item.dto.BrandResponse;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ward.ward_server.global.exception.ExceptionCode.BRAND_NOT_FOUND;
import static com.ward.ward_server.global.exception.ExceptionCode.DUPLICATE_BRAND;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {
    private final BrandRepository brandRepository;
    private final ItemRepository itemRepository;

    public BrandResponse createBrand(BrandRequest request) {
        if (request.name() == null || request.name().isBlank() || request.logoImage() == null || request.logoImage().isBlank())
            throw new ApiException(ExceptionCode.INVALID_INPUT);
        if (brandRepository.existsByName(request.name())) throw new ApiException(DUPLICATE_BRAND);
        Brand savedBrand = brandRepository.save(Brand.builder()
                .logoImage(request.logoImage())
                .name(request.name())
                .build());
        return new BrandResponse(savedBrand.getLogoImage(), savedBrand.getName(), savedBrand.getViewCount(), savedBrand.getWishCount());
    }

    @Transactional(readOnly = true)
    public List<BrandItemResponse> getBrandTop10AndItem3List() {
        List<Brand> top10brandList = brandRepository.findBrandListTop10();
        Map<Long, List<ItemSimpleResponse>> top3ItemListByBrand = top10brandList.stream()
                .collect(
                        Collectors.toMap(
                                Brand::getId,
                                brand -> itemRepository.findBrandItemListTop3(brand.getId()).stream()
                                        .map(item -> new ItemSimpleResponse(item.getItemImages().get(0).getUrl(), item.getName(), item.getCode()))
                                        .toList()
                        ));
        return top10brandList.stream()
                .map(brand -> new BrandItemResponse(brand.getLogoImage(), brand.getName(), top3ItemListByBrand.get(brand.getId())))
                .toList();
    }

    @Transactional
    public BrandResponse updateBrand(String originBrandName, BrandRequest request) {
        if (request.name() == null && request.logoImage() == null) throw new ApiException(ExceptionCode.INVALID_INPUT);
        Brand brand = brandRepository.findByName(originBrandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        if (request.logoImage() != null && !request.logoImage().isBlank()) brand.updateLogoImage(request.logoImage());
        if (request.name() != null && !request.name().isBlank()) {
            if (brandRepository.existsByName(request.name())) throw new ApiException(DUPLICATE_BRAND);
            brand.updateName(request.name());
        }
        return new BrandResponse(brand.getLogoImage(), brand.getName(), brand.getViewCount(), brand.getWishCount());
    }

    @Transactional
    public void deleteBrand(String brandName) {
        if (!brandRepository.existsByName(brandName)) throw new ApiException(BRAND_NOT_FOUND);
        brandRepository.deleteByName(brandName);
    }
}
