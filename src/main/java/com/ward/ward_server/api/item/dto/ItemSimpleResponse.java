package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemSimpleResponse(
        String itemName,
        String itemCode,
        String itemImage,
        String brandName
) {
}
