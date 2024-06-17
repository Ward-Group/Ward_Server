package com.ward.ward_server.api.item.repository.query;

import com.ward.ward_server.api.item.dto.ItemSimpleResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemQueryRepository {
    List<ItemSimpleResponse> getDueTodayItemOrdered(long userId, LocalDateTime date);

    List<ItemSimpleResponse> getReleaseTodayItemOrdered(long userId, LocalDateTime date);

    List<ItemSimpleResponse> getReleaseWishItemOrdered(long userId, LocalDateTime date);

    List<ItemSimpleResponse> getJustConfirmReleaseItemOrdered(long userId, LocalDateTime date);

    List<ItemSimpleResponse> getRegisterTodayItemOrdered(long userId, LocalDateTime date);
}
