package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.TopBrandResponse;
import com.ward.ward_server.api.item.dto.BrandRequest;
import com.ward.ward_server.api.item.dto.BrandResponse;
import com.ward.ward_server.api.item.service.BrandService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
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
        return ApiResponse.ok(BRAND_CREATE_SUCCESS, brandService.createBrand(request.brandName(), request.brandLogoImage()));
    }

    @GetMapping("/top10")
    public ApiResponse<List<TopBrandResponse>> getBrandTop10AndItem3List(){
        return ApiResponse.ok(BRAND_TOP10_WITH_ITEM_LOAD_SUCCESS, brandService.getBrandTop10AndItem3List());
    }

    @PatchMapping("/{originBrandName}")
    public ApiResponse<BrandResponse> updateBrand(@PathVariable("originBrandName") String originBrandName, @RequestBody BrandRequest request) {
        return ApiResponse.ok(BRAND_UPDATE_SUCCESS, brandService.updateBrand(originBrandName, request.brandName(), request.brandLogoImage()));
    }

    @DeleteMapping("/{brandName}")
    public ApiResponse deleteBrand(@PathVariable("brandName") String brandName){
        brandService.deleteBrand(brandName);
        return ApiResponse.ok(BRAND_DELETE_SUCCESS);
    }

    @PatchMapping("/{brandName}/view-counts")
    public ApiResponse<Integer> increaseBrandViewCount(@PathVariable("brandName") String brandName){
        return ApiResponse.ok(BRAND_VIEW_COUNT_UP_SUCCESS, brandService.increaseBrandViewCount(brandName));
    }
}
