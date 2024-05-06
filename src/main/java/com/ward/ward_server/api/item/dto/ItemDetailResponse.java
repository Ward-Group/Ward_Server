package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemDetailResponse(
        String itemName,
        String itemCode,
        List<String> itemImages,
        String brandName,
        long viewCount,
        String category,
        int price
) {
}
