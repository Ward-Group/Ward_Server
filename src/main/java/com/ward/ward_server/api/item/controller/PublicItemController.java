package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.item.dto.ItemDetailResponse;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.dto.ItemTopRankResponse;
import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.api.item.scheduler.ItemViewCountScheduler;
import com.ward.ward_server.api.item.service.ItemService;
import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.Object.enums.Section;
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
@RequestMapping("/public/items")
public class PublicItemController {

    private final ItemService itemService;
    private final ItemViewCountScheduler itemViewCountScheduler;

    @GetMapping("/{itemId}/details")
    public ApiResponse<ItemDetailResponse> getItem(@PathVariable("itemId") Long itemId) {
        return ApiResponse.ok(ITEM_DETAIL_LOAD_SUCCESS, itemService.getItem(itemId));
    }

    @GetMapping("/{section}/home")
    public ApiResponse<List<ItemSimpleResponse>> getItem10List(@AuthenticationPrincipal CustomUserDetails principal,
                                                               @PathVariable("section") Section section,
                                                               @RequestParam("category") Category category) {
        return ApiResponse.ok(ITEM_LIST_LOAD_SUCCESS, itemService.getItem10List(principal.getUserId(), section, category));
    }

    @GetMapping("/{section}")
    public ApiResponse<PageResponse<ItemSimpleResponse>> getItemPage(@AuthenticationPrincipal CustomUserDetails principal,
                                                                     @PathVariable("section") Section section,
                                                                     @RequestParam("category") Category category,
                                                                     @Positive @RequestParam("page") int page,
                                                                     @RequestParam(value = "date", defaultValue = "none") String date) {
        return ApiResponse.ok(ITEM_LIST_LOAD_SUCCESS, itemService.getItemPage(principal.getUserId(), section, category, page - 1, date));
    }

    @GetMapping("/top")
    public ApiResponse<List<ItemTopRankResponse>> getTopItemsByCategory(@RequestParam("category") Category category,
                                                                        @RequestParam("limit") int limit) {
        if (limit != 10 && limit != 50) {
            throw new ApiException(ExceptionCode.INVALID_INPUT, "Limit 는 10 or 50 이어야합니다.");
        }

        List<ItemTopRankResponse> topItemsResponse = itemService.getTopItemsResponseByCategory(category, limit);
        return ApiResponse.ok(REALTIME_TOP_LOAD_SUCCESS, topItemsResponse);
    }
}
