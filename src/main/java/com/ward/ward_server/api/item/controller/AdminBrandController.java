package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.BrandRequest;
import com.ward.ward_server.api.item.dto.BrandResponse;
import com.ward.ward_server.api.item.service.BrandService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/brands")
public class AdminBrandController {
    private final BrandService brandService;

    @PostMapping
    public ApiResponse<BrandResponse> createBrand(@ModelAttribute BrandRequest request) throws IOException {
        return ApiResponse.ok(BRAND_CREATE_SUCCESS, brandService.createBrand(request.koreanName(), request.englishName(), request.logoImage()));
    }

    @PatchMapping("/{brandId}")
    public ApiResponse<BrandResponse> updateBrand(@PathVariable("brandId") long brandId, @ModelAttribute BrandRequest request) throws IOException {
        return ApiResponse.ok(BRAND_UPDATE_SUCCESS, brandService.updateBrand(brandId, request.koreanName(), request.englishName(), request.logoImage()));
    }

    @DeleteMapping("/{brandId}")
    public ApiResponse<Void> deleteBrand(@PathVariable("brandId") long brandId) {
        brandService.deleteBrand(brandId);
        return ApiResponse.ok(BRAND_DELETE_SUCCESS);
    }
}
