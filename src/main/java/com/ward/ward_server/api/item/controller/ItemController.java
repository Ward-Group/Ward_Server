package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.ItemCreateRequest;
import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.dto.ItemUpdateRequest;
import com.ward.ward_server.api.item.service.ItemService;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.web.bind.annotation.*;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

//FIXME 관리자권한으로만 접근하도록 조정
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ApiResponse<ItemDetailResponse> createItem(@Valid @RequestBody ItemCreateRequest itemCreateRequest) throws ApiException {
        return ApiResponse.ok(ITEM_CREATE_SUCCESS, itemService.createItem(itemCreateRequest));
    }

    @GetMapping
    public ApiResponse<ItemDetailResponse> getItem(@RequestParam(value = "itemCode") String itemCode,
                                                   @RequestParam(value = "brandName") String brandName) {
        return ApiResponse.ok(ITEM_DETAIL_LOAD_SUCCESS, itemService.getItem(itemCode, brandName));
    }

    @GetMapping("/list")
    public ApiResponse<PageResponse<ItemSimpleResponse>> getItemList(@Positive @RequestParam(value = "page", defaultValue = "1") int page,
                                                                     @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return ApiResponse.ok(ITEM_LIST_LOAD_SUCCESS, itemService.getItemList(page - 1, size));
    }

    @PatchMapping
    public ApiResponse<ItemDetailResponse> updateItem(@RequestParam(value = "itemCode") String itemCode,
                                                      @RequestParam(value = "brandName") String brandName,
                                                      @RequestBody ItemUpdateRequest itemUpdateRequest) {
        return ApiResponse.ok(ITEM_UPDATE_SUCCESS, itemService.updateItem(itemCode, brandName, itemUpdateRequest));
    }

    @DeleteMapping
    public ApiResponse deleteItem(@RequestParam(value = "itemCode") String itemCode,
                                  @RequestParam(value = "brandName") String brandName) {
        itemService.deleteItem(itemCode, brandName);
        return ApiResponse.ok(ITEM_DELETE_SUCCESS);
    }

}
