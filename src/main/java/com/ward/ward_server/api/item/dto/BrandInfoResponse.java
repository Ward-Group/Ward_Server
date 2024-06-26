package com.ward.ward_server.api.item.dto;

import java.util.List;

public record BrandInfoResponse(
        long brandId,
        String brandLogoImage,
        String brandKoreanName,
        String brandEnglishName,
        long brandViewCount,
        long brandWishCount,
        List<BrandItemMainImageResponse> itemList
) {
}
