package com.ward.ward_server.api.item.dto;

import java.util.List;

public record BrandItemResponse(
        String brandLogoImage,
        String brandName,
        List<ItemSimpleResponse> top3itemList
) {
}
