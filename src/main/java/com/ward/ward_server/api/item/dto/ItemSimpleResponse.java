package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemSimpleResponse(
        long itemId,
        String itemKoreanName,
        String itemEnglishName,
        String itemCode,
        String itemMainImage,
        long brandId,
        String brandKoreanName,
        String brandEnglishName,
        boolean isWished
) {
}
