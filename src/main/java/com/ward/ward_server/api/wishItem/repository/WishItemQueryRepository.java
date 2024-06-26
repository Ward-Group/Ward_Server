package com.ward.ward_server.api.wishItem.repository;

import com.ward.ward_server.api.wishItem.WishItemResponse;
import com.ward.ward_server.global.Object.enums.ApiSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishItemQueryRepository {
    Page<WishItemResponse> getWishItemPage(long userId, ApiSort apiSort, Pageable pageable);
}
