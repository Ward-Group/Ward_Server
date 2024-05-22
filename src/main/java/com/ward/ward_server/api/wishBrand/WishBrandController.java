package com.ward.ward_server.api.wishBrand;

import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.response.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wish-brands")
public class WishBrandController {
    private final WishBrandService wishBrandService;

    @PostMapping
    public ApiResponse createWishBrand(@AuthenticationPrincipal CustomUserDetails principal,
                                       @NotBlank @RequestParam(value = "brandName") String brandName) {
        wishBrandService.createWishBrand(principal.getUserId(), brandName);
        return ApiResponse.ok(WISH_BRAND_CREATE_SUCCESS);
    }

    @GetMapping
    public ApiResponse<PageResponse<WishBrandResponse>> getWishBrandListByUser(
            @Positive @RequestParam(value = "page", defaultValue = "1") int page,
            @Positive @RequestParam(value = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ApiResponse.ok(WISH_BRAND_LOAD_SUCCESS, wishBrandService.getWishBrandListByUser(page - 1, size, principal.getUserId()));
    }

    @DeleteMapping
    public ApiResponse deleteWishBrand(@AuthenticationPrincipal CustomUserDetails principal,
                                       @NotBlank @RequestParam(value = "brandName") String brandName) {
        wishBrandService.deleteWishBrand(principal.getUserId(), brandName);
        return ApiResponse.ok(WISH_BRAND_DELETE_SUCCESS);
    }
}
