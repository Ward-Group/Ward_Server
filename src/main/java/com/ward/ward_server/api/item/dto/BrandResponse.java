package com.ward.ward_server.api.item.dto;

public record BrandResponse(
        long id,
        String brandLogoImage,
        String koreanName,
        String englishName,
        long viewCount
) {
}
