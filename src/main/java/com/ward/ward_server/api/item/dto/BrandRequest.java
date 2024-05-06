package com.ward.ward_server.api.item.dto;

import jakarta.validation.constraints.NotBlank;

public record BrandRequest(
        String brandName,
        String brandLogoImage
) {
}
