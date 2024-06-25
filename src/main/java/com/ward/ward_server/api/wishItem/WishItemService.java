package com.ward.ward_server.api.wishItem;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.item.repository.ItemImageRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class WishItemService {
    private final WishItemRepository wishItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;

    @Transactional
    public void createWishItem(long userId, long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ApiException(ITEM_NOT_FOUND));
        if (wishItemRepository.existsByUserIdAndItemId(userId, item.getId())) {
            throw new ApiException(DUPLICATE_WISH_ITEM);
        }
        wishItemRepository.save(new WishItem(user, item));
    }

    @Transactional(readOnly = true)
    public PageResponse<WishItemResponse> getWishItemListByUser(int page, int size, long userId) {
        Page<WishItem> wishItemPage = wishItemRepository.findAllByUserId(userId, PageRequest.of(page, size));
        List<WishItem> contents = wishItemPage.getContent();
        List<WishItemResponse> responses = contents.stream()
                .map(e -> new WishItemResponse(
                        itemImageRepository.findFirstByItemId(e.getItem().getId()).get().getUrl(),
                        e.getItem().getBrand().getKoreanName(),
                        e.getItem().getKoreanName(),
                        e.getItem().getCode(),
                        e.getItem().getPrice()))
                .toList();
        return new PageResponse<>(responses, wishItemPage);
    }

    @Transactional
    public void deleteWishItem(long userId, long itemId) {
        if (!itemImageRepository.existsById(itemId)) {
            throw new ApiException(ITEM_NOT_FOUND);
        }
        if (!wishItemRepository.existsByUserIdAndItemId(userId, itemId)) {
            throw new ApiException(WISH_ITEM_NOT_FOUND);
        }
        wishItemRepository.deleteByUserIdAndItemId(userId, itemId);
    }
}
