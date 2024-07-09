package com.ward.ward_server.api.item.dto;

public record ItemTopRankResponse(
        int rank,
        Long itemId,
        String mainImage,
        String brandName,
        String koreanName
) {
}
