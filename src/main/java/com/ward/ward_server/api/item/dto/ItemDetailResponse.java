package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemDetailResponse(
        long itemId,
        String itemKoreanName,
        String itemEnglishName,
        String itemCode,
        String MainImage,
        List<String> itemImages,
        long brandId,
        String brandKoreanName,
        String brandEnglishName,
        long viewCount,
        String category,
        int price
) {
}
