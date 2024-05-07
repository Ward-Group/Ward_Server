package com.ward.ward_server.api.wishBrand;

public record WishBrandResponse(
        String brandLogoImage,
        String brandName,
        int wishCount
) {
}