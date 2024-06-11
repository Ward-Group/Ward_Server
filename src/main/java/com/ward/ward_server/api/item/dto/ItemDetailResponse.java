package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemDetailResponse(
        String itemKoreanName,
        String itemEnglishName,
        String itemCode,
        List<String> itemImages,
        String brandKoreanName,
        String brandEnglishName,
        long viewCount,
        String category,
        int price
) {
}
