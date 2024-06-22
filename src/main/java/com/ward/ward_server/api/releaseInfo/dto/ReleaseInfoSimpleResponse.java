package com.ward.ward_server.api.releaseInfo.dto;

import com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod;

import java.time.LocalDateTime;

import static com.ward.ward_server.global.Object.Constants.FORMAT;

public record ReleaseInfoSimpleResponse(
        String platformKoreanName,
        String platformEnglishName,
        long itemId,
        String itemMainImage,
        String itemKoreanName,
        String itemEnglishName,
        String releaseMethod,
        String dueDate
) {
    public ReleaseInfoSimpleResponse(String platformKoreanName,
                                     String platformEnglishName,
                                     long itemId,
                                     String itemMainImage,
                                     String itemKoreanName,
                                     String itemEnglishName,
                                     ReleaseMethod releaseMethod,
                                     LocalDateTime dueDate) {
        this(platformKoreanName,
                platformEnglishName,
                itemId,
                itemMainImage,
                itemKoreanName,
                itemEnglishName,
                releaseMethod.getDesc(),
                dueDate.format(FORMAT));
    }
}
