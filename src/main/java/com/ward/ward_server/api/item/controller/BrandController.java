package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.BrandInfoResponse;
import com.ward.ward_server.api.item.dto.BrandRequest;
import com.ward.ward_server.api.item.dto.BrandResponse;
import com.ward.ward_server.api.item.service.BrandService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    public ApiResponse<BrandResponse> createBrand(@RequestBody BrandRequest request) {
        return ApiResponse.ok(BRAND_CREATE_SUCCESS, brandService.createBrand(request.koreanName(), request.englishName(), request.brandLogoImage()));
    }

    @GetMapping("/top10")
    public ApiResponse<Page<BrandInfoResponse>> getBrandItemPage(@Positive @RequestParam(value = "page", defaultValue = "1") int page) {
        return ApiResponse.ok(BRAND_TOP10_WITH_ITEM_LOAD_SUCCESS, brandService.getBrandItemPage(page - 1));
    }

    @PatchMapping("/{originBrandName}")
    public ApiResponse<BrandResponse> updateBrand(@PathVariable("originBrandName") String originBrandName, @RequestBody BrandRequest request) {
        return ApiResponse.ok(BRAND_UPDATE_SUCCESS, brandService.updateBrand(originBrandName, request.koreanName(), request.englishName(), request.brandLogoImage()));
    }

    @DeleteMapping("/{brandName}")
    public ApiResponse<Void> deleteBrand(@PathVariable("brandName") String brandName) {
        brandService.deleteBrand(brandName);
        return ApiResponse.ok(BRAND_DELETE_SUCCESS);
    }

    @PatchMapping("/{brandName}/view-counts")
    public ApiResponse<Long> increaseBrandViewCount(@PathVariable("brandName") String brandName) {
        return ApiResponse.ok(BRAND_VIEW_COUNT_UP_SUCCESS, brandService.increaseBrandViewCount(brandName));
    }
}
