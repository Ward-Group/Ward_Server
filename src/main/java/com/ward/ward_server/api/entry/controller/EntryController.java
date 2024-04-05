package com.ward.ward_server.api.entry.controller;

import com.ward.ward_server.api.entry.service.EntryService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/entry")
public class EntryController {

    private final EntryService entryService;

    //응모 내역 추가
    @PostMapping("/create")
    public ApiResponse create(@RequestBody EntryRequestDTO entryRequestDTO) {
        Long entryId = entryService.createEntry(entryRequestDTO);
        return ApiResponse.ok(entryId);
    }

//    //응모 내역 조회 - 보고 있는 상세 아이템 1건의 응모 내역을 확인하기 위해
//    @GetMapping("/{entryId}")
//    public ApiResponse getEntry(@PathVariable Long entryId) {
//        EntryRecord entryRecord = entryService.getEntryById(entryId);
//        return ApiResponse.ok();
//    }
//
    // TODO 응모 내역 전체 조회 기능
    //응모 내역 조회 - 개별 사용자의 전체 리스트
//    @GetMapping("/user/{userId}")
//    public ApiResponse getUsersEntryRecord(@PathVariable("userId") Long userId) {
//        // userId를 사용하여 해당 사용자의 전체 응모 내역 조회
//        List<EntryRecord> entryRecords = entryService.getUsersEntryRecord(userId);
//        return ApiResponse.ok(entryRecords);
//    }
//
//    // 응모 내역 제거
//    @DeleteMapping("/{entryId}/delete")
//    public ApiResponse deleteEntry(@PathVariable Long entryId) {
//        // entryId를 사용하여 해당 응모 내역 삭제
//        entryService.deleteEntry(entryId);
//        return ApiResponse.ok();
//    }

}
