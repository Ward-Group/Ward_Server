package com.ward.ward_server.api.item.dto;

public record BrandItemResponse(
        String itemKoreanName,
        String itemEnglishName,
        String itemCode,
        String itemMainImage,
        long itemViewCount,
        long itemWishCount) {
}
