package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemRequest(
        String itemName,
        String itemCode,
        List<String> itemImages,
        String brandName,
        String category,
        Integer price) {
}