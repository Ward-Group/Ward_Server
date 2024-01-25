package com.ward.ward_server.webcrawling;

import com.ward.ward_server.webcrawling.enumtype.Brand;

import java.time.LocalDateTime;

public record WebProductData(
        String name,
        String imgUrl,
        String siteUrl,
        LocalDateTime releaseDate,
        LocalDateTime dueDate,
        LocalDateTime presentationDate,
        Brand brand
){}
