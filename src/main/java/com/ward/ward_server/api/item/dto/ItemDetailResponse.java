package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemDetailResponse(
        long itemId,
        String itemKoreanName,
        String itemEnglishName,
        String itemCode,
        String mainImage,
        List<String> itemImages,
        long viewCount,
        String category,
        int price,
        long brandId,
        String brandLogoImage,
        String brandKoreanName,
        String brandEnglishName
) {
}
