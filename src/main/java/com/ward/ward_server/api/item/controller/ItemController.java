package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemRequest;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.enumtype.ItemSort;
import com.ward.ward_server.api.item.service.ItemService;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

//FIXME 관리자권한으로만 접근하도록 조정
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ApiResponse<ItemDetailResponse> createItem(@RequestBody ItemRequest request) throws ApiException {
        return ApiResponse.ok(ITEM_CREATE_SUCCESS,
                itemService.createItem(request.itemName(), request.itemCode(), request.itemImages(), request.brandName(), request.category(), request.price()));
    }

    @GetMapping
    public ApiResponse<ItemDetailResponse> getItem(@RequestParam(value = "item-code") String itemCode,
                                                   @RequestParam(value = "brand-name") String brandName) {
        return ApiResponse.ok(ITEM_DETAIL_LOAD_SUCCESS, itemService.getItem(itemCode, brandName));
    }

    @GetMapping("/top10")
    public ApiResponse<List<ItemSimpleResponse>> getHomeItem10List(@RequestParam ItemSort sort) {
        return ApiResponse.ok(ITEM_LIST_LOAD_SUCCESS, itemService.getItemTop10List(sort));
    }

    @GetMapping("/page")
    public ApiResponse<PageResponse<ItemSimpleResponse>> getItemPageOrdered(@Positive @RequestParam(value = "page", defaultValue = "1") int page,
                                                                            @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.ok(ITEM_PAGE_LOAD_SUCCESS, itemService.getItemPageOrdered(page - 1, size));
    }

    @PatchMapping
    public ApiResponse<ItemDetailResponse> updateItem(@RequestParam(value = "origin-item-code") String originItemCode,
                                                      @RequestParam(value = "origin-brand-name") String originBrandName,
                                                      @RequestBody ItemRequest request) {
        return ApiResponse.ok(ITEM_UPDATE_SUCCESS,
                itemService.updateItem(originItemCode, originBrandName, request.itemName(), request.itemCode(), request.itemImages(), request.brandName(), request.category(), request.price()));
    }

    @DeleteMapping
    public ApiResponse<Void> deleteItem(@RequestParam(value = "item-code") String itemCode,
                                        @RequestParam(value = "brand-name") String brandName) {
        itemService.deleteItem(itemCode, brandName);
        return ApiResponse.ok(ITEM_DELETE_SUCCESS);
    }

}
