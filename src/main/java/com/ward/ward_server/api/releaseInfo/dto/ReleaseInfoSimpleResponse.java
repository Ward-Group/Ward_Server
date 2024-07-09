package com.ward.ward_server.api.releaseInfo.dto;

import com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod;

import java.time.LocalDateTime;

import static com.ward.ward_server.global.Object.Constants.DATE_STRING_FORMAT;

public record ReleaseInfoSimpleResponse(
        long releaseInfoId,
        String platformKoreanName,
        String platformEnglishName,
        long itemId,
        String itemMainImage,
        String itemKoreanName,
        String itemEnglishName,
        String releaseMethod,
        String releaseDate,
        String dueDate
) {
    public ReleaseInfoSimpleResponse(long releaseInfoId,
                                     String platformKoreanName,
                                     String platformEnglishName,
                                     long itemId,
                                     String itemMainImage,
                                     String itemKoreanName,
                                     String itemEnglishName,
                                     ReleaseMethod releaseMethod,
                                     LocalDateTime releaseDate,
                                     LocalDateTime dueDate) {
        this(releaseInfoId,
                platformKoreanName,
                platformEnglishName,
                itemId,
                itemMainImage,
                itemKoreanName,
                itemEnglishName,
                releaseMethod.getDesc(),
                releaseDate.format(DATE_STRING_FORMAT),
                dueDate.format(DATE_STRING_FORMAT));
    }
}
