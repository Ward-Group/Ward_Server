package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.enumtype.ItemSort;

import java.util.List;

public interface ItemQueryRepository {
    List<ItemSimpleResponse> getDueTodayItem10Ordered();
    List<ItemSimpleResponse> getReleaseTodayItem10Ordered();
    List<ItemSimpleResponse> getReleaseWishItem10Ordered(long userId);
    List<ItemSimpleResponse> getNotReleaseItem10Ordered();
    List<ItemSimpleResponse> getRegisterTodayItem10Ordered();
}
