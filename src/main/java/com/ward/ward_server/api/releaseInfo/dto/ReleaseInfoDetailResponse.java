package com.ward.ward_server.api.releaseInfo.dto;

import java.time.LocalDateTime;

public record ReleaseInfoDetailResponse(
        String itemCode,
        String itemName,
        int price,
        String drawPlatform,
        String siteUrl,
        LocalDateTime releaseDate,
        LocalDateTime dueDate,
        LocalDateTime presentationDate,
        String status
) {
}
