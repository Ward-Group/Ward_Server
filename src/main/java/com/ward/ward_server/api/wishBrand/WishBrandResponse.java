package com.ward.ward_server.api.wishBrand;

public record WishBrandResponse(
        long brandId,
        String logoImage,
        String koreanName,
        String englishName
) {
}