package com.ward.ward_server.api.item.dto;

public record BrandItemMainImageResponse(
        long itemId,
        String itemMainImage,
        long itemViewCount,
        long itemWishCount) {
}
