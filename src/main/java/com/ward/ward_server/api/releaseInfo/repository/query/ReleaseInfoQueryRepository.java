package com.ward.ward_server.api.releaseInfo.repository.query;

import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.global.Object.enums.HomeSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ReleaseInfoQueryRepository {
    List<ReleaseInfoSimpleResponse> getHomeSortList(long userId, LocalDateTime now, Category category, HomeSort homeSort);

    Page<ReleaseInfoSimpleResponse> getHomeSortPage(long userId, LocalDateTime now, Category category, HomeSort homeSort, Pageable pageable);
    Page<ReleaseInfoSimpleResponse> getBrandReleaseInfoPage(long brandId, Pageable pageable);
}
