package com.ward.ward_server.api.wishItem;

public record WishItemResponse(
        String brandKoreanName,
        String brandEnglishName,
        long itemId,
        String itemMainImage,
        String itemKoreanName,
        String itemEnglishName,
        boolean isEntry
) {
}
