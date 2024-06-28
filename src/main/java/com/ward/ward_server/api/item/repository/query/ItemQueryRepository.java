package com.ward.ward_server.api.item.repository.query;

import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.global.Object.enums.HomeSort;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemQueryRepository {
    List<ItemSimpleResponse> getHomeSortList(Long userId, LocalDateTime now, Category category, HomeSort homeSort);
}
