package com.ward.ward_server.api.item.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ItemImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    @Getter
    private String url;

    @Builder
    public ItemImage(Long itemId, String url){
        this.itemId=itemId;
        this.url=url;
    }
}
