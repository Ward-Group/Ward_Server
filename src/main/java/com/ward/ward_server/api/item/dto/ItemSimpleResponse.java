package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemSimpleResponse(
        String name,
        String code,
        String imageUrl,
        String brand
) {
}
