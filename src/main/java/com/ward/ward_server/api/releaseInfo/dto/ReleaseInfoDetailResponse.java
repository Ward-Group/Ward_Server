package com.ward.ward_server.api.releaseInfo.dto;

import java.time.LocalDateTime;

public record ReleaseInfoDetailResponse(
        String itemCode,
        String itemName,
        int price,
        String platformLogoImage,
        String platformName,
        String siteUrl,
        String releaseDate,
        String dueDate,
        String presentationDate,
        String status
) {
}
