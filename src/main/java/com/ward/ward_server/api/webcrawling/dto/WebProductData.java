package com.ward.ward_server.api.webcrawling.dto;

import com.ward.ward_server.api.webcrawling.entity.enumtype.Brand;
import com.ward.ward_server.api.webcrawling.entity.enumtype.State;

import java.time.LocalDateTime;

public record WebProductData(
        String name,
        String imgUrl,
        String siteUrl,
        LocalDateTime releaseDate,
        LocalDateTime dueDate,
        LocalDateTime presentationDate,
        State state,
        Brand brand
){}
