package com.ward.ward_server.api.item.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ItemCreateRequest(
        @NotBlank String itemName,
        @NotBlank String itemCode,
        List<String> itemImages,
        @NotBlank String brandName,
        @NotBlank String category,
        int price
) {
}
