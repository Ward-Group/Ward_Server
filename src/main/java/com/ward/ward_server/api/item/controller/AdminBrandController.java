package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.BrandRequest;
import com.ward.ward_server.api.item.dto.BrandResponse;
import com.ward.ward_server.api.item.service.BrandService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/brands")
public class AdminBrandController {
    private final BrandService brandService;

    @PostMapping
    public ApiResponse<BrandResponse> createBrand(@RequestPart BrandRequest request,
                                                  @RequestPart(required = false) MultipartFile logoImage) throws IOException {
        return ApiResponse.ok(BRAND_CREATE_SUCCESS, brandService.createBrand(request.koreanName(), request.englishName(), logoImage));
    }

    @PatchMapping("/{brandId}")
    public ApiResponse<BrandResponse> updateBrand(@PathVariable("brandId") long brandId,
                                                  @RequestPart(required = false) BrandRequest request,
                                                  @RequestPart(required = false) MultipartFile logoImage) throws IOException {
        if (request == null) {
            return ApiResponse.ok(BRAND_UPDATE_SUCCESS, brandService.updateBrand(brandId, null, null, logoImage));
        } else {
            return ApiResponse.ok(BRAND_UPDATE_SUCCESS, brandService.updateBrand(brandId, request.koreanName(), request.englishName(), logoImage));
        }
    }

    @DeleteMapping("/{brandId}")
    public ApiResponse<Void> deleteBrand(@PathVariable("brandId") long brandId) {
        brandService.deleteBrand(brandId);
        return ApiResponse.ok(BRAND_DELETE_SUCCESS);
    }
}
