package com.ward.ward_server.api.wishlist.dto;

import com.ward.ward_server.api.item.dto.ItemDTO;
import com.ward.ward_server.api.wishlist.domain.Wishlist;
import lombok.Data;

import java.util.Date;

@Data
public class WishlistResponseDTO {

    private Long wishlistId;
    private String name;
    private ItemDTO item;
    private Date wishlistDate;

    public WishlistResponseDTO(Wishlist wishlist) {
        this.wishlistId = wishlist.getWishlistId();
        this.name = wishlist.getUser().getName();
        this.item = new ItemDTO(wishlist.getItem());
        this.wishlistDate = wishlist.getWishDate();
    }
}
