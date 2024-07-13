package com.ward.ward_server.api.entry.dto;

public record EntryDetailResponse(
        String mainImage,
        String englishName,
        String koreanName,
        String code
) {
}
