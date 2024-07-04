package com.ward.ward_server.api.item.repository.query;

import com.ward.ward_server.api.item.dto.BrandItemResponse;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.global.Object.enums.BasicSort;
import com.ward.ward_server.global.Object.enums.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemQueryRepository {
    List<ItemSimpleResponse> getItem10List(Long userId, LocalDateTime now, Category category, Section section);

    Page<ItemSimpleResponse> getItemPage(Long userId, LocalDateTime now, Category category, Section section, String yearMonth, Pageable pageable);

    Page<BrandItemResponse> getBrandItemPage(long brandId, BasicSort basicSort, Pageable pageable);
}
