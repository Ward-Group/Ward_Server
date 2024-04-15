package com.ward.ward_server.api.wishlist.controller;

import com.ward.ward_server.api.wishlist.dto.WishlistRequestDTO;
import com.ward.ward_server.api.wishlist.service.WishlistService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wish")
public class WishlistController {

    private final WishlistService wishlistService;

    // TODO 추가, 삭제, 조회

    // 추가
    @PostMapping("/create")
    public ApiResponse<Long> createWishlist(@RequestBody WishlistRequestDTO wishlistRequestDTO) {
        Long wishlist = wishlistService.createWishlist(wishlistRequestDTO);
        return ApiResponse.ok(wishlist);
    }

    // 삭제

    // 전체 조회
}
