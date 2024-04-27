package com.ward.ward_server.api.releaseInfo.dto;

import java.time.LocalDateTime;

public record ReleaseInfoSimpleResponse(
        String imageUrl,
        String drawPlatform,
        String siteUrl,
        LocalDateTime dueDate,
        String status,
        boolean isEntry
) {
}
