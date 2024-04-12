package com.ward.ward_server.api.item.controller;

import com.ward.ward_server.api.entry.service.EntryService;
import com.ward.ward_server.api.item.dto.ItemResponseDTO;
import com.ward.ward_server.api.item.service.ItemService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {


    private final EntryService entryService;
    private final ItemService itemService;

    // 상품 1개 상세 페이지
    @PostMapping("/detail")
    public ApiResponse<ItemResponseDTO> getItemDetail(
            @RequestBody ItemRequsetDTO requestDTO,
            Principal principal) {

        Long itemId = requestDTO.getItemId();
        Long userId = requestDTO.getUserId();
        // TODO itemId 만 받아서 Principal 에서 꺼내서도 가능한지 체크
//        Long userId = (principal != null) ? Long.parseLong(principal.getName()) : null;

        ItemResponseDTO responseDTO = itemService.getItemDetail(itemId, userId);

        return ApiResponse.ok(responseDTO);
    }

    // 상품 전체
}
