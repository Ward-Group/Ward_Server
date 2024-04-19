package com.ward.ward_server.api.wishlist.dto;

import lombok.Data;

@Data
public class WishlistRequestDTO {
    private long userId;
    private long itemId;
}
