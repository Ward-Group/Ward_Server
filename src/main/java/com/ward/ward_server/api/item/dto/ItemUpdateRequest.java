package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemUpdateRequest(
        String itemName,
        String itemCode,
        String brandName,
        String category,
        int price
) {
}
