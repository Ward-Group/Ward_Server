package com.ward.ward_server.api.wishItem.dto;

public record WishItemResponse(
        String itemImage,
        String brandName,
        String itemName,
        String itemCode,
        int price
) {
}
