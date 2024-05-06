package com.ward.ward_server.api.item.dto;

public record BrandResponse(
        String logoImage,
        String name,
        long viewCount,
        long wishCount
) {
}
