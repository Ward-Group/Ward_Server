package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemUpdateRequest(
        String name,
        String code,
        String brand,
        String category,
        int price
) {
}
