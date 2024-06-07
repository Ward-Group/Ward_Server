package com.ward.ward_server.api.item.dto;

import java.util.List;

public record BrandInfoResponse(
        String brandLogoImage,
        String brandKoreanName,
        String brandEnglishName,
        Integer brandViewCount,
        Integer brandWishCount,
        List<BrandItemResponse> itemList
) {
}
