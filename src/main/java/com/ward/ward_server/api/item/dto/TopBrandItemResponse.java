package com.ward.ward_server.api.item.dto;

public record TopBrandItemResponse(
        String itemName,
        String itemCode,
        String itemImage,
        Long itemViewCount,
        Long itemWishCount) {
}
