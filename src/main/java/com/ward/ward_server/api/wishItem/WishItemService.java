package com.ward.ward_server.api.wishItem;

import com.ward.ward_server.api.entry.repository.EntryRecordRepository;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.api.wishItem.repository.WishItemRepository;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.Object.enums.ApiSort;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static com.ward.ward_server.global.Object.Constants.API_PAGE_SIZE;
import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishItemService {
    private final WishItemRepository wishItemRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final EntryRecordRepository entryRecordRepository;
    private final ReleaseInfoRepository releaseInfoRepository;

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
    public PageResponse<WishItemResponse> getWishItemListByUser(long userId, ApiSort sort, int page) {
        Page<WishItemResponse> wishItemPage = wishItemRepository.getWishItemPage(userId, sort, PageRequest.of(page, API_PAGE_SIZE));
        return new PageResponse<>(wishItemPage.getContent(), wishItemPage);
    }

    private Set<Long> getEntryItemIdsByUser(long userId) {
        Set<Long> releaseInfoIds = new HashSet<>(entryRecordRepository.findEntryReleaseInfoIdsByUser(userId));
        return new HashSet<>(releaseInfoRepository.findItemIdsIn(releaseInfoIds));
    }

    @Transactional
    public void deleteWishItem(long userId, long itemId) {
        if (!wishItemRepository.existsByUserIdAndItemId(userId, itemId)) {
            throw new ApiException(WISH_ITEM_NOT_FOUND);
        }
        wishItemRepository.deleteByUserIdAndItemId(userId, itemId);
    }
}
