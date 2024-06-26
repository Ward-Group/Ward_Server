package com.ward.ward_server.api.releaseInfo.repository.query;

import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.global.Object.enums.HomeSort;

import java.time.LocalDateTime;
import java.util.List;

public interface ReleaseInfoQueryRepository {
    List<ReleaseInfoSimpleResponse> getHomeSortList(long userId, LocalDateTime now, Category category, HomeSort homeSort);
}
