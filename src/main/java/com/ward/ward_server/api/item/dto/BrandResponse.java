package com.ward.ward_server.api.item.dto;

public record BrandResponse(
        String brandLogoImage,
        String brandName,
        long viewCount
) {
}
