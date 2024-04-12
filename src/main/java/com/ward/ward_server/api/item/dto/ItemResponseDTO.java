package com.ward.ward_server.api.item.dto;

import com.ward.ward_server.api.item.entity.Item;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemResponseDTO {

    private Long id;
    private String name;
    private String image;
    private String siteUrl;
    private LocalDateTime releaseDate;
    private LocalDateTime dueDate;
    private LocalDateTime presentationDate;
    private String state;
    private String brand;
    private Boolean isEntryExist;  // 응모 여부 추가

    public ItemResponseDTO(Item item) {
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
