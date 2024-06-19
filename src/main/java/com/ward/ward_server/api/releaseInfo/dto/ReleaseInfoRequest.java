package com.ward.ward_server.api.releaseInfo.dto;

import com.ward.ward_server.api.releaseInfo.entity.enums.CurrencyUnit;
import com.ward.ward_server.api.releaseInfo.entity.enums.DeliveryMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.NotificationMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod;
import jakarta.validation.constraints.Pattern;

public record ReleaseInfoRequest(
        String itemCode,
        String brandName,
        String platformName,
        String siteUrl,
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}", message = "yyyy-MM-dd HH:mm 형식으로 입력해주세요")
        String releaseDate,
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}", message = "yyyy-MM-dd HH:mm 형식으로 입력해주세요")
        String dueDate,
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}", message = "yyyy-MM-dd HH:mm 형식으로 입력해주세요")
        String presentationDate,
        Integer releasePrice,
        CurrencyUnit currencyUnit,
        NotificationMethod notificationMethod,
        ReleaseMethod releaseMethod,
        DeliveryMethod deliveryMethod
) {
}
