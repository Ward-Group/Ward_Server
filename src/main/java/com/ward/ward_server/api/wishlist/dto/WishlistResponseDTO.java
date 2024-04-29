package com.ward.ward_server.api.wishlist.dto;

//import com.ward.ward_server.api.item.dto.ItemDto;
import com.ward.ward_server.api.wishlist.domain.Wishlist;
import lombok.Data;

import java.util.Date;

@Data
public class WishlistResponseDTO {

    private Long wishlistId;
    private String name;
    //private ItemDto item;
    //FIXME 잠시주석처리
    private Date wishlistDate;

    public WishlistResponseDTO(Wishlist wishlist) {
        this.wishlistId = wishlist.getWishlistId();
        this.name = wishlist.getUser().getName();
        //this.item = new ItemDto(wishlist.getItem());
        this.wishlistDate = wishlist.getWishDate();
    }
}
