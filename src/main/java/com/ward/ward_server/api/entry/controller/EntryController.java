package com.ward.ward_server.api.entry.controller;

import com.ward.ward_server.api.entry.domain.EntryRecord;
import com.ward.ward_server.api.entry.dto.EntryRecordResponseDTO;
import com.ward.ward_server.api.entry.dto.EntryRequestDTO;
import com.ward.ward_server.api.entry.service.EntryService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/entries")
public class EntryController {

    private final EntryService entryService;

    //응모 내역 추가
    @PostMapping
    public ApiResponse create(@RequestBody EntryRequestDTO entryRequestDTO) {
        Long entryId = entryService.createEntry(entryRequestDTO);
        return ApiResponse.ok(entryId);
    }

    // 응모 내역 제거
    @DeleteMapping
    public ApiResponse deleteEntry(@RequestBody EntryRequestDTO entryRequestDTO) {
        entryService.deleteEntry(entryRequestDTO);
        return ApiResponse.ok();
    }

    //응모 1건 내역 조회 - 보고 있는 상세 아이템 1건의 응모 여부 확인
    @GetMapping("/{entryId}")
    public ApiResponse getEntry(@PathVariable("entryId") Long entryId) {
        EntryRecord entryRecord = entryService.getEntryById(entryId);
        return ApiResponse.ok(entryRecord);
    }

    //응모 내역 전체 조회 - 개별 사용자의 전체 리스트
    @GetMapping("/user/{userId}")
    public ApiResponse<List<EntryRecordResponseDTO>> getUsersEntryRecord(@PathVariable("userId") Long userId) {
        // userId를 사용하여 해당 사용자의 전체 응모 내역 조회
        List<EntryRecord> entryRecords = entryService.getUsersAllEntryRecord(userId);
        List<EntryRecordResponseDTO> result = entryRecords.stream()
                .map(e -> new EntryRecordResponseDTO(e))
                .collect(Collectors.toList());

        return ApiResponse.ok(result);
    }

    // 응모 여부 확인
    @GetMapping("/check")
    public ApiResponse<Boolean> checkEntryExist(@RequestBody EntryRequestDTO entryRequestDTO) {
        boolean isEntryExist = entryService.isEntryExist(entryRequestDTO.getUserId(), entryRequestDTO.getItemId());
        return ApiResponse.ok(isEntryExist);
    }

}
