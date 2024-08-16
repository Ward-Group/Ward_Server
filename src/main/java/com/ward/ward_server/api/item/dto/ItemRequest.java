package com.ward.ward_server.api.item.dto;

import com.ward.ward_server.api.item.entity.enums.Category;

public record ItemRequest(
        String koreanName,
        String englishName,
        String itemCode,
        Long brandId,
        Category category,
        Integer price) {
}