package com.ward.ward_server.api.item.dto;

import com.ward.ward_server.api.item.entity.Item;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemDTO {

    private Long id;
    private String name;
    private String image;
    private String siteUrl;
    private LocalDateTime releaseDate;
    private LocalDateTime dueDate;
    private LocalDateTime presentationDate;
    private String state;
    private String brand;

    public ItemDTO(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.image = item.getImage();
        this.siteUrl = item.getSiteUrl();
        this.releaseDate = item.getReleaseDate();
        this.dueDate = item.getDueDate();
        this.presentationDate = item.getPresentationDate();
        this.state = item.getState().toString();
        this.brand = item.getBrand().toString();
    }
}
