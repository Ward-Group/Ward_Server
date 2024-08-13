package com.ward.ward_server.api.item.dto;

import org.springframework.web.multipart.MultipartFile;

public record BrandRequest(
        String koreanName,
        String englishName,
        MultipartFile logoImage
) {
}
