package com.ward.ward_server.api.releaseInfo.dto;

public record ReleaseInfoIdentificationRequest(
        Long itemId,
        String platformName
) {
}
