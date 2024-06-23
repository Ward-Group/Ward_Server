package com.ward.ward_server.api.releaseInfo.dto;

public record ReleaseInfoIdentifierRequest(
        Long itemId,
        String platformName
) {
}
