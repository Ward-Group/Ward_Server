package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.ItemCreateRequest;
import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.dto.ItemUpdateRequest;
import com.ward.ward_server.api.item.service.ItemService;
import com.ward.ward_server.global.entity.PageResponse;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.response.ApiResponse;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.web.bind.annotation.*;

//FIXME 관리자권한으로만 접근하도록 조정
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ApiResponse<ItemDetailResponse> createItem(@RequestBody ItemCreateRequest itemCreateRequest) throws ApiException {
        ItemDetailResponse response = itemService.createItem(itemCreateRequest);
        return ApiResponse.ok(ITEM_CREATE_SUCCESS, response);
    }

    @GetMapping("/{itemCode}")
    public ApiResponse<ItemDetailResponse> getItem(@PathVariable("itemCode") String itemCode)  {
        ItemDetailResponse response = itemService.getItem(itemCode);
        return ApiResponse.ok(ITEM_DETAIL_LOAD_SUCCESS, response);
    }

    @GetMapping
    public ApiResponse<PageResponse<ItemSimpleResponse>> getItemList(@Positive @RequestParam(value = "page", defaultValue = "1") int page,
                                                                     @Positive @RequestParam(value = "size", defaultValue = "5") int size){
        PageResponse<ItemSimpleResponse> response=itemService.getItemList(page - 1, size);
        return ApiResponse.ok(ITEM_LIST_LOAD_SUCCESS, response);
    }

    @PatchMapping("/{itemCode}")
    public ApiResponse<ItemDetailResponse> updateItem(@PathVariable("itemCode") String itemCode, @RequestBody ItemUpdateRequest itemUpdateRequest){
        ItemDetailResponse response=itemService.updateItem(itemCode, itemUpdateRequest);
        return ApiResponse.ok(ITEM_UPDATE_SUCCESS, response);
    }

    @DeleteMapping("/{itemCode}")
    public ApiResponse deleteItem(@PathVariable("itemCode") String itemCode){
        itemService.deleteItem(itemCode);
        return ApiResponse.ok(ITEM_DELETE_SUCCESS);
    }

}
