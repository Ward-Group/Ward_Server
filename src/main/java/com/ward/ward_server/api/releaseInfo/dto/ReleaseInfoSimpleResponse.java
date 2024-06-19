package com.ward.ward_server.api.releaseInfo.dto;

import java.time.LocalDateTime;

public record ReleaseInfoSimpleResponse(
        String platformLogoImage,
        String platformName,
        String siteUrl,
        String dueDate
) {
}
