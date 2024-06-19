package com.ward.ward_server.api.releaseInfo.dto;

import com.ward.ward_server.api.releaseInfo.entity.enums.DeliveryMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.NotificationMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod;

import java.time.LocalDateTime;

public record ReleaseInfoDetailResponse(
        String brandKoreanName,
        String brandEnglishName,
        String itemCode,
        String itemKoreanName,
        String itemEnglishName,
        String itemMainImage,
        String platformLogoImage,
        String platformKoreanName,
        String platformEnglishName,
        String releaseDate,
        String dueDate,
        String presentationDate,
        Integer releasePrice,
        String currentUnit,
        String notificationMethod,
        String releaseMethod,
        String deliveryMethod
) {
}
