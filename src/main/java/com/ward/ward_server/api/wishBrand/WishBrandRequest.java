package com.ward.ward_server.api.wishBrand;

import jakarta.validation.constraints.NotNull;

public record WishBrandRequest(
        @NotNull long brandId
) {
}
