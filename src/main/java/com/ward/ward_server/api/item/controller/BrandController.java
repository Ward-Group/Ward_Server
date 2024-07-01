package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.*;
import com.ward.ward_server.api.item.service.BrandService;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
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
        return ApiResponse.ok(BRAND_AND_ITEM3_PAGE_LOAD_SUCCESS, brandService.getBrandAndItem3Page(sort, page - 1));
    }

    @GetMapping("/recommended")
    public ApiResponse<List<BrandRecommendedResponse>> getRecommendedBrands() {
        return ApiResponse.ok(BRAND_RECOMMENDED_LOAD_SUCCESS, brandService.getRecommendedBrands());
    }

    @GetMapping("/{brandId}/items")
    public ApiResponse<PageResponse<BrandItemResponse>> getBrandItemPage(@PathVariable("brandId") long brandId,
                                                                         @RequestParam("sort") BasicSort sort,
                                                                         @Positive @RequestParam(value = "page") int page) {
        return ApiResponse.ok(BRAND_ITEM_PAGE_LOAD_SUCCESS, brandService.getBrandItemPage(brandId, sort, page - 1));
    }

    @GetMapping("/{brandId}/release-infos")
    public ApiResponse<PageResponse<ReleaseInfoSimpleResponse>> getBrandReleaseInfoPage(@PathVariable("brandId") long brandId,
                                                                                        @Positive @RequestParam(value = "page") int page) {
        return ApiResponse.ok(BRAND_RELEASE_INFO_PAGE_LOAD_SUCCESS, brandService.getBrandReleaseInfoPage(brandId, page - 1));
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
