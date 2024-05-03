package com.ward.ward_server.api.releaseInfo.dto;

import jakarta.validation.constraints.NotNull;

public record DrawPlatformRequest(
        String name,
        String logoImage
) {
}
