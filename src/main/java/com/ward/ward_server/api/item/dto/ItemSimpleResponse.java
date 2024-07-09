package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemSimpleResponse(
        long itemId,
        String itemKoreanName,
        String itemEnglishName,
        Integer price,
        String itemMainImage,
        long brandId,
        String brandKoreanName,
        String brandEnglishName,
        boolean isWished
) {
}
