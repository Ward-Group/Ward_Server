package com.ward.ward_server.api.search.controller;

import com.ward.ward_server.api.search.entity.SearchHistory;
import com.ward.ward_server.api.search.service.SearchHistoryService;
import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search-history")
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @GetMapping
    public ApiResponse<List<SearchHistory>> getSearchHistory(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<SearchHistory> searchHistories = searchHistoryService.getSearchHistory(userDetails.getUserId());
        return ApiResponse.ok(searchHistories);
    }
}
