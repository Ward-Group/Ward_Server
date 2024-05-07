package com.ward.ward_server.api.wishItem;

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
@RequestMapping("/wish-items")
public class WishItemController {
    private final WishItemService wishItemService;

    @PostMapping
    public ApiResponse createWishItem(@AuthenticationPrincipal CustomUserDetails principal,
                                      @NotBlank @RequestParam(value = "itemCode") String itemCode) {
        wishItemService.createWishItem(principal.getUserId(), itemCode);
        return ApiResponse.ok(WISH_ITEM_CREATE_SUCCESS);
    }

    @GetMapping
    public ApiResponse<PageResponse<WishItemResponse>> getWishItemListByUser(
            @Positive @RequestParam(value = "page", defaultValue = "1") int page,
            @Positive @RequestParam(value = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ApiResponse.ok(WISH_ITEM_LOAD_SUCCESS, wishItemService.getWishItemListByUser(page - 1, size, principal.getUserId()));
    }

    @DeleteMapping
    public ApiResponse deleteWishlist(@AuthenticationPrincipal CustomUserDetails principal,
                                      @NotBlank @RequestParam(value = "itemCode") String itemCode) {
        wishItemService.deleteWishItem(principal.getUserId(), itemCode);
        return ApiResponse.ok(WISH_ITEM_DELETE_SUCCESS);
    }
}
