package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemRequest;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.dto.ItemTopRankResponse;
import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.api.item.service.ItemService;
import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.Object.enums.HomeSort;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ApiResponse<ItemDetailResponse> createItem(@RequestBody ItemRequest request) throws ApiException {
        return ApiResponse.ok(ITEM_CREATE_SUCCESS,
                itemService.createItem(request.itemCode(), request.koreanName(), request.englishName(), request.mainImage(), request.itemImages(), request.brandId(), request.category(), request.price()));
    }

    @GetMapping("/{itemId}/details")
    public ApiResponse<ItemDetailResponse> getItem(@PathVariable("itemId") Long itemId) {
        return ApiResponse.ok(ITEM_DETAIL_LOAD_SUCCESS, itemService.getItem(itemId));
    }

    @GetMapping("/home")
    public ApiResponse<List<ItemSimpleResponse>> getItem10List(@AuthenticationPrincipal CustomUserDetails principal,
                                                               @RequestParam("sort") HomeSort sort,
                                                               @RequestParam("category") Category category) {
        return ApiResponse.ok(ITEM_LIST_LOAD_SUCCESS, itemService.getItem10List(principal.getUserId(), sort, category));
    }

    @GetMapping
    public ApiResponse<PageResponse<ItemSimpleResponse>> getItemPage(@AuthenticationPrincipal CustomUserDetails principal,
                                                                     @RequestParam("sort") HomeSort sort,
                                                                     @RequestParam("category") Category category,
                                                                     @Positive @RequestParam("page") int page) {
        return ApiResponse.ok(ITEM_LIST_LOAD_SUCCESS, itemService.getItemPage(principal.getUserId(), sort, category, page - 1));
    }

    @GetMapping("/top")
    public ApiResponse<List<ItemTopRankResponse>> getTopItemsByCategory(@RequestParam("category") String category,
                                                                        @RequestParam("limit") int limit) {
        if (limit != 10 && limit != 50) {
            throw new ApiException(ExceptionCode.INVALID_INPUT, "Limit 는 10 or 50 이어야합니다.");
        }

        List<ItemTopRankResponse> topItemsResponse = itemService.getTopItemsResponseByCategory(category, limit);
        return ApiResponse.ok(REALTIME_TOP_LOAD_SUCCESS, topItemsResponse);
    }

    @PatchMapping("/{itemId}")
    public ApiResponse<ItemDetailResponse> updateItem(@PathVariable("itemId") Long itemId,
                                                      @RequestBody ItemRequest request) {
        return ApiResponse.ok(ITEM_UPDATE_SUCCESS,
                itemService.updateItem(itemId, request.koreanName(), request.englishName(), request.itemCode(), request.mainImage(), request.itemImages(), request.brandId(), request.category(), request.price()));
    }

    @DeleteMapping("/{itemId}")
    public ApiResponse<Void> deleteItem(@PathVariable("itemId") Long itemId) {
        itemService.deleteItem(itemId);
        return ApiResponse.ok(ITEM_DELETE_SUCCESS);
    }
}
