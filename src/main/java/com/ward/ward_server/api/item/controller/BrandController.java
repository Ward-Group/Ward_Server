package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.BrandItemResponse;
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
        return ApiResponse.ok(BRAND_CREATE_SUCCESS, brandService.createBrand(request));
    }

    @GetMapping("/top10")
    public ApiResponse<List<BrandItemResponse>> getBrandTop10AndItem3List(){
        return ApiResponse.ok(BRAND_TOP10_WITH_ITEM_LOAD_SUCCESS, brandService.getBrandTop10AndItem3List());
    }

    @PatchMapping("/{brandName}")
    public ApiResponse<BrandResponse> updateBrand(@PathVariable("brandName") String brandName, @RequestBody BrandRequest request) {
        return ApiResponse.ok(BRAND_UPDATE_SUCCESS, brandService.updateBrand(brandName, request));
    }

    @DeleteMapping
    public ApiResponse deleteBrand(@PathVariable("brandName") String brandName){
        brandService.deleteBrand(brandName);
        return ApiResponse.ok(BRAND_DELETE_SUCCESS);
    }
}
