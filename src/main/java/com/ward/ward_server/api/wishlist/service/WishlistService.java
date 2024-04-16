package com.ward.ward_server.api.wishlist.service;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.api.wishlist.domain.Wishlist;
import com.ward.ward_server.api.wishlist.dto.WishlistRequestDTO;
import com.ward.ward_server.api.wishlist.repository.WishlistRepository;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    // 중복 조회
    public boolean isWishlistExist(Long userId, Long itemId) {
        return wishlistRepository.existsByUserIdAndItemId(userId, itemId);
    }

    // 추가
    @Transactional
    public Long createWishlist(WishlistRequestDTO wishlistRequestDTO) {

        long userId = wishlistRequestDTO.getUserId();
        long itemId = wishlistRequestDTO.getItemId();

        // 요청된 userId,itemId 로 이미 관심 목록 기록이 있는지 확인
        boolean wishlistExist = isWishlistExist(userId, itemId);
        if (wishlistExist) {
            throw new ApiException(DUPLICATE_WISHLIST); // 이미 등록한 경우 에러 발생
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));

        Wishlist wishlist = new Wishlist(user, item);

        wishlistRepository.save(wishlist);

        return wishlist.getWishlistId();
    }

    // 삭제
    @Transactional
    public void deleteWishlist(WishlistRequestDTO wishlistRequestDTO) {

        long userId = wishlistRequestDTO.getUserId();
        long itemId = wishlistRequestDTO.getItemId();

        boolean wishlistExist = isWishlistExist(userId, itemId);
        if (!wishlistExist) {
            throw new ApiException(NO_WISHLIST_FOUND);
        }

        wishlistRepository.deleteByUserIdAndItemId(userId, itemId);
    }

}
