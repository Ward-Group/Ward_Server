package com.ward.ward_server.api.item.dto;

public record BrandItemResponse(
        long itemId,
        String mainImage,
        String koreanName,
        String englishName,
        Integer price
) {
}
