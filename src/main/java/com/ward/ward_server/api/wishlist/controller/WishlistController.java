package com.ward.ward_server.api.wishlist.controller;

import com.ward.ward_server.api.wishlist.domain.Wishlist;
import com.ward.ward_server.api.wishlist.dto.WishlistRequestDTO;
import com.ward.ward_server.api.wishlist.dto.WishlistResponseDTO;
import com.ward.ward_server.api.wishlist.service.WishlistService;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wish")
public class WishlistController {

    private final WishlistService wishlistService;

    // 추가
    @PostMapping("/create")
    public ApiResponse<Long> createWishlist(@RequestBody WishlistRequestDTO wishlistRequestDTO) {
        Long wishlist = wishlistService.createWishlist(wishlistRequestDTO);
        return ApiResponse.ok(wishlist);
    }

    // 삭제
    @DeleteMapping("/delete")
    public ApiResponse deleteWishlist(@RequestBody WishlistRequestDTO wishlistRequestDTO) {
        wishlistService.deleteWishlist(wishlistRequestDTO);
        return ApiResponse.ok();
    }

    // 관심 목록 전체 조회 - 개별 사용자의 전체 리스트
    @GetMapping("/user/{userId}")
    public ApiResponse<List<WishlistResponseDTO>> getUsersWishlist(@PathVariable("userId") Long userId) {
        // userId를 사용하여 해당 사용자의 전체 응모 내역 조회
        List<Wishlist> usersAllWishlist = wishlistService.getUsersAllWishlist(userId);
        List<WishlistResponseDTO> result = usersAllWishlist.stream()
                .map(w -> new WishlistResponseDTO(w))
                .collect(Collectors.toList());

        return ApiResponse.ok(result);
    }


    // 관심 목록 존재 유무 확인
    @GetMapping("/check")
    public ApiResponse<Boolean> checkEntryExist(@RequestBody WishlistRequestDTO wishlistRequestDTO) {
        boolean wishlistExist = wishlistService.isWishlistExist(wishlistRequestDTO.getUserId(), wishlistRequestDTO.getItemId());
        return ApiResponse.ok(wishlistExist);
    }
}
