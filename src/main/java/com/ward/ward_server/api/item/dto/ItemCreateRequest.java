package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemCreateRequest(
        String name,
        String code,
        List<String> imageUrls,
        String brand,
        String category,
        int price
) {
}
