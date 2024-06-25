package com.ward.ward_server.api.item.dto;

public record BrandResponse(
        long id,
        String logoImage,
        String koreanName,
        String englishName,
        long viewCount
) {
}
