package com.ward.ward_server.api.item.dto;

import com.ward.ward_server.api.item.entity.enums.Category;

import java.util.List;

public record ItemRequest(
        String koreanName,
        String englishName,
        String itemCode,
        String mainImage,
        List<String> itemImages,
        String brandName,
        Category category,
        Integer price) {
}