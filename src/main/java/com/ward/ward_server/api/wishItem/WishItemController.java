package com.ward.ward_server.api.wishItem;

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
@RequestMapping("/user/wish-items")
public class WishItemController {
    private final WishItemService wishItemService;

    @PostMapping
    public ApiResponse<Void> createWishItem(@AuthenticationPrincipal CustomUserDetails principal,
                                            @RequestBody WishItemRequest request) {
        wishItemService.createWishItem(principal.getUserId(), request.itemId());
        return ApiResponse.ok(WISH_ITEM_CREATE_SUCCESS);
    }

    @GetMapping
    public ApiResponse<PageResponse<WishItemResponse>> getWishItemListByUser(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam("sort") BasicSort sort,
            @Positive @RequestParam(value = "page") int page) {
        return ApiResponse.ok(WISH_ITEM_LOAD_SUCCESS, wishItemService.getWishItemListByUser(principal.getUserId(), sort, page - 1));
    }

    @DeleteMapping("/{itemId}")
    public ApiResponse<Void> deleteWishlist(@AuthenticationPrincipal CustomUserDetails principal,
                                            @PathVariable("itemId") long itemId) {
        wishItemService.deleteWishItem(principal.getUserId(), itemId);
        return ApiResponse.ok(WISH_ITEM_DELETE_SUCCESS);
    }
}
