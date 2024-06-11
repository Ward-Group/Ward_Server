package com.ward.ward_server.api.item.dto;

import java.util.List;

public record BrandInfoResponse(
        String brandLogoImage,
        String brandKoreanName,
        String brandEnglishName,
        long brandViewCount,
        long brandWishCount,
        List<BrandItemResponse> itemList
) {
}
