package com.ward.ward_server.api.item.dto;

import java.util.List;

public record TopBrandResponse(
        String brandLogoImage,
        String brandName,
        Long brandViewCount,
        Long brandWishCount,
        List<TopBrandItemResponse> top3itemList
) {
}
