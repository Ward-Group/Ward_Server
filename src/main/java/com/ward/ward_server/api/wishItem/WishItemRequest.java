package com.ward.ward_server.api.wishItem;

import jakarta.validation.constraints.NotNull;

public record WishItemRequest(
        @NotNull long itemId
) {
}
