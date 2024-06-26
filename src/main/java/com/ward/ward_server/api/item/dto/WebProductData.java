package com.ward.ward_server.api.item.dto;

import com.ward.ward_server.api.item.entity.Brand;

import java.time.LocalDateTime;

public record WebProductData(
        String name,
        String imgUrl,
        String siteUrl,
        LocalDateTime releaseDate,
        LocalDateTime dueDate,
        LocalDateTime presentationDate,
        Brand brand
) {
}
