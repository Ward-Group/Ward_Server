package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemRequest(
        String koreanName,
        String englishName,
        String itemCode,
        String mainImage,
        List<String> itemImages,
        String brandName,
        String category,
        Integer price) {
}