package com.ward.ward_server.api.item.dto;

public record ItemTopResponse(
        int rank,
        String mainImage,
        String brandName,
        String koreanName
) {
}
