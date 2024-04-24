package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemDetailResponse(
        String name,
        String code,
        List<String> imageUrls,
        String brand,
        long viewCount,
        String category,
        int price
) {
}
