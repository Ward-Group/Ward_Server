package com.ward.ward_server.api.wishBrand.repository;

import com.ward.ward_server.api.wishBrand.WishBrandResponse;
import com.ward.ward_server.global.Object.enums.BasicSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishBrandQueryRepository {
    Page<WishBrandResponse> getWishBrandPage(long userId, BasicSort basicSort, Pageable pageable);
}
