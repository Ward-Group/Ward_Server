package com.ward.ward_server.api.entry.controller;

import com.ward.ward_server.api.entry.dto.EntryCountResponse;
import com.ward.ward_server.api.entry.service.EntryRecordService;
import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ward.ward_server.global.response.ApiResponseMessage.ENTRY_COUNTS_FETCH_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public/entry-records")
public class SharedEntryRecordController {

    private final EntryRecordService entryRecordService;

    @GetMapping("/count")
    public ApiResponse<EntryCountResponse> getEntryCounts(@AuthenticationPrincipal CustomUserDetails principal) {
        if (principal == null) {
            // 비회원인 경우, 기본값으로 반환
            return ApiResponse.ok(ENTRY_COUNTS_FETCH_SUCCESS, new EntryCountResponse(0, 0, 0, 0));
        }
        return ApiResponse.ok(ENTRY_COUNTS_FETCH_SUCCESS, entryRecordService.getEntryCounts(principal.getUserId()));
    }
}
