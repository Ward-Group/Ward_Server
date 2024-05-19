package com.ward.ward_server.api.entry.controller;

import com.ward.ward_server.api.entry.dto.EntryRecordDetailResponse;
import com.ward.ward_server.api.entry.dto.EntryRecordRequest;
import com.ward.ward_server.api.entry.dto.EntryRecordResponse;
import com.ward.ward_server.api.entry.service.EntryRecordService;
import com.ward.ward_server.api.user.auth.security.CustomUserDetails;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.response.ApiResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ward.ward_server.global.response.ApiResponseMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/entryRecords")
public class EntryRecordController {

    private final EntryRecordService entryRecordService;

    @PostMapping
    public ApiResponse<EntryRecordResponse> createEntryRecord(@AuthenticationPrincipal CustomUserDetails principal, @RequestBody EntryRecordRequest request) {
        return ApiResponse.ok(ENTRY_RECORD_CREATE_SUCCESS, entryRecordService.createEntryRecord(principal.getUserId(), request));
    }

    @DeleteMapping
    public ApiResponse deleteEntryRecord(@AuthenticationPrincipal CustomUserDetails principal,
                                         @RequestParam(value = "itemCode") String itemCode,
                                         @RequestParam(value = "brandName") String brandName,
                                         @RequestParam(value = "platformName") String platformName) {
        entryRecordService.deleteEntryRecord(principal.getUserId(), itemCode, brandName, platformName);
        return ApiResponse.ok(ENTRY_RECORD_DELETE_SUCCESS);
    }

    @GetMapping("/item")
    public ApiResponse<List<EntryRecordDetailResponse>> getEntryRecordListByItem(@AuthenticationPrincipal CustomUserDetails principal,
                                                                                 @RequestParam(value = "itemCode") String itemCode,
                                                                                 @RequestParam(value = "brandName") String brandName) {
        return ApiResponse.ok(ENTRY_RECORD_BY_ITEM_LOAD_SUCCESS, entryRecordService.getEntryRecordListByItem(principal.getUserId(), itemCode, brandName));
    }

    @GetMapping("/user")
    public ApiResponse<PageResponse<EntryRecordDetailResponse>> getEntryRecordListByUser(@AuthenticationPrincipal CustomUserDetails principal,
                                                                                         @Positive @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                         @Positive @RequestParam(value = "size", defaultValue = "5") int size) {
        return ApiResponse.ok(ENTRY_RECORD_BY_USER_LOAD_SUCCESS, entryRecordService.getEntryRecordListByUser(principal.getUserId(), page - 1, size));
    }
}
