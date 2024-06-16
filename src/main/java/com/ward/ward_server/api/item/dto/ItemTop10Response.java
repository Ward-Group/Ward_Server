package com.ward.ward_server.api.item.dto;

public record ItemTop10Response(
        int rank,
        String mainImage,
        String brandName,
        String koreanName
) {
}
