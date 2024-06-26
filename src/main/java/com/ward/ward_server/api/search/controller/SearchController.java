package com.ward.ward_server.api.search.controller;

import com.ward.ward_server.api.search.dto.SearchItemsResponse;
import com.ward.ward_server.api.search.service.SearchService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/items")
    public ApiResponse<SearchItemsResponse> searchItems(@RequestParam("keyword") String keyword,
                                                        @RequestParam("page") int page,
                                                        @RequestParam("size") int size) {
        SearchItemsResponse searchResults = searchService.searchItems(keyword, page, size);
        return ApiResponse.ok(searchResults);
    }
}
