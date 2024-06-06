package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.enumtype.ItemSort;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemQueryRepository {
    List<ItemSimpleResponse> getDueTodayItem10Ordered(long userId, LocalDateTime date);
    List<ItemSimpleResponse> getReleaseTodayItem10Ordered(long userId, LocalDateTime date);
    List<ItemSimpleResponse> getReleaseWishItem10Ordered(long userId, LocalDateTime date);
    List<ItemSimpleResponse> getNotReleaseItem10Ordered(long userId, LocalDateTime date);
    List<ItemSimpleResponse> getRegisterTodayItem10Ordered(long userId, LocalDateTime date);
}
