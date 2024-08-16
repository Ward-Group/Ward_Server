package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemRequest;
import com.ward.ward_server.api.item.scheduler.ItemViewCountScheduler;
import com.ward.ward_server.api.item.service.ItemService;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/items")
public class AdminItemController {

    private final ItemService itemService;
    private final ItemViewCountScheduler itemViewCountScheduler;

    @PostMapping("/execute-update-view-counts")
    public ApiResponse<String> abc() {
        itemViewCountScheduler.executeUpdateViewCounts();
        return ApiResponse.ok("성공");
    }


    @PostMapping
    public ApiResponse<ItemDetailResponse> createItem(@RequestPart ItemRequest request,
                                                      @RequestPart(required = false) MultipartFile mainImage,
                                                      @RequestPart(required = false) List<MultipartFile> itemImages) throws ApiException, IOException {
        return ApiResponse.ok(ITEM_CREATE_SUCCESS,
                itemService.createItem(request.itemCode(), request.koreanName(), request.englishName(), mainImage, itemImages, request.brandId(), request.category(), request.price()));
    }

    @PatchMapping("/{itemId}")
    public ApiResponse<ItemDetailResponse> updateItem(@PathVariable("itemId") Long itemId,
                                                      @RequestPart(required = false) ItemRequest request,
                                                      @RequestPart(required = false) MultipartFile mainImage,
                                                      @RequestPart(required = false) List<MultipartFile> itemImages) throws IOException {
        if (request == null) {
            return ApiResponse.ok(ITEM_UPDATE_SUCCESS,
                    itemService.updateItem(itemId,
                            null, null, null, null, null, null,
                            mainImage, itemImages));
        } else {
            return ApiResponse.ok(ITEM_UPDATE_SUCCESS,
                    itemService.updateItem(itemId,
                            request.koreanName(), request.englishName(), request.itemCode(), request.brandId(), request.category(), request.price(),
                            mainImage, itemImages));
        }
    }

    @DeleteMapping("/{itemId}")
    public ApiResponse<Void> deleteItem(@PathVariable("itemId") Long itemId) {
        itemService.deleteItem(itemId);
        return ApiResponse.ok(ITEM_DELETE_SUCCESS);
    }
}
