package com.ward.ward_server.api.item.dto;

import com.ward.ward_server.api.item.entity.enumtype.Brand;
import com.ward.ward_server.api.item.entity.enumtype.Status;

import java.time.LocalDateTime;

public record WebProductData(
        String name,
        String imgUrl,
        String siteUrl,
        LocalDateTime releaseDate,
        LocalDateTime dueDate,
        LocalDateTime presentationDate,
        Status status,
        Brand brand
) {
}
