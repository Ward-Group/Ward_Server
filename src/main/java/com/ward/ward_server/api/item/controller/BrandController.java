package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.BrandInfoResponse;
import com.ward.ward_server.api.item.dto.BrandRecommendedResponse;
import com.ward.ward_server.api.item.dto.BrandRequest;
import com.ward.ward_server.api.item.dto.BrandResponse;
import com.ward.ward_server.api.item.service.BrandService;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.Object.enums.BasicSort;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;

    @PostMapping
    public ApiResponse<BrandResponse> createBrand(@RequestBody BrandRequest request) {
        return ApiResponse.ok(BRAND_CREATE_SUCCESS, brandService.createBrand(request.koreanName(), request.englishName(), request.logoImage()));
    }

    @GetMapping
    public ApiResponse<PageResponse<BrandInfoResponse>> getBrandAndItem3Page(@RequestParam("sort") BasicSort sort,
                                                                             @Positive @RequestParam(value = "page") int page) {
        return ApiResponse.ok(BRAND_TOP10_WITH_ITEM_LOAD_SUCCESS, brandService.getBrandAndItem3Page(sort, page - 1));
    }

    @GetMapping("/recommended")
    public ApiResponse<List<BrandRecommendedResponse>> getRecommendedBrands() {
        return ApiResponse.ok(BRAND_RECOMMENDED_LOAD_SUCCESS, brandService.getRecommendedBrands());
    }

    @PatchMapping("/{brandId}")
    public ApiResponse<BrandResponse> updateBrand(@PathVariable("brandId") long brandId, @RequestBody BrandRequest request) {
        return ApiResponse.ok(BRAND_UPDATE_SUCCESS, brandService.updateBrand(brandId, request.koreanName(), request.englishName(), request.logoImage()));
    }

    @DeleteMapping("/{brandId}")
    public ApiResponse<Void> deleteBrand(@PathVariable("brandId") long brandId) {
        brandService.deleteBrand(brandId);
        return ApiResponse.ok(BRAND_DELETE_SUCCESS);
    }

    @PatchMapping("/{brandId}/view-counts")
    public ApiResponse<Long> increaseBrandViewCount(@PathVariable("brandId") long brandId) {
        return ApiResponse.ok(BRAND_VIEW_COUNT_UP_SUCCESS, brandService.increaseBrandViewCount(brandId));
    }
}
