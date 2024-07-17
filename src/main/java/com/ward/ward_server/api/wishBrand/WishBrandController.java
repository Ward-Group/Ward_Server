package com.ward.ward_server.api.wishBrand;

import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.Object.enums.BasicSort;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/wish-brands")
public class WishBrandController {
    private final WishBrandService wishBrandService;

    @PostMapping
    public ApiResponse<Void> createWishBrand(@AuthenticationPrincipal CustomUserDetails principal,
                                             @RequestBody WishBrandRequest request) {
        wishBrandService.createWishBrand(principal.getUserId(), request.brandId());
        return ApiResponse.ok(WISH_BRAND_CREATE_SUCCESS);
    }

    @GetMapping
    public ApiResponse<PageResponse<WishBrandResponse>> getWishBrandListByUser(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam("sort") BasicSort sort,
            @Positive @RequestParam(value = "page") int page) {
        return ApiResponse.ok(WISH_BRAND_LOAD_SUCCESS, wishBrandService.getWishBrandListByUser(principal.getUserId(), sort, page - 1));
    }

    @DeleteMapping("/{brandId}")
    public ApiResponse<Void> deleteWishBrand(@AuthenticationPrincipal CustomUserDetails principal,
                                             @PathVariable("brandId") long brandId) {
        wishBrandService.deleteWishBrand(principal.getUserId(), brandId);
        return ApiResponse.ok(WISH_BRAND_DELETE_SUCCESS);
    }
}
