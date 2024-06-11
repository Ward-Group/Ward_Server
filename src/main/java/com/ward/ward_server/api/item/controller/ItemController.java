package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemRequest;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.enumtype.ItemSort;
import com.ward.ward_server.api.item.service.ItemService;
import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
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
                itemService.createItem(request.itemCode(), request.koreanName(), request.englishName(), request.mainImage(), request.itemImages(), request.brandName(), request.category(), request.price()));
    }

    @GetMapping("/details")
    public ApiResponse<ItemDetailResponse> getItem(@RequestParam(value = "item-code") String itemCode,
                                                   @RequestParam(value = "brand-name") String brandName) {
        return ApiResponse.ok(ITEM_DETAIL_LOAD_SUCCESS, itemService.getItem(itemCode, brandName));
    }

    @GetMapping
    public ApiResponse<List<ItemSimpleResponse>> getItem10List(@AuthenticationPrincipal CustomUserDetails principal,
                                                               @RequestParam ItemSort sort) {
        return ApiResponse.ok(ITEM_LIST_LOAD_SUCCESS, itemService.getItem10List(principal.getUserId(), sort));
    }

    @PatchMapping
    public ApiResponse<ItemDetailResponse> updateItem(@RequestParam(value = "origin-item-code") String originItemCode,
                                                      @RequestParam(value = "origin-brand-name") String originBrandName,
                                                      @RequestBody ItemRequest request) {
        return ApiResponse.ok(ITEM_UPDATE_SUCCESS,
                itemService.updateItem(originItemCode, originBrandName, request.koreanName(), request.englishName(), request.itemCode(), request.mainImage(), request.itemImages(), request.brandName(), request.category(), request.price()));
    }

    @DeleteMapping
    public ApiResponse<Void> deleteItem(@RequestParam(value = "item-code") String itemCode,
                                        @RequestParam(value = "brand-name") String brandName) {
        itemService.deleteItem(itemCode, brandName);
        return ApiResponse.ok(ITEM_DELETE_SUCCESS);
    }

}
