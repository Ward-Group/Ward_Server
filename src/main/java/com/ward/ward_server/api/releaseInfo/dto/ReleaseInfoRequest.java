package com.ward.ward_server.api.releaseInfo.dto;

import jakarta.validation.constraints.Pattern;
import org.antlr.v4.runtime.misc.NotNull;

public record ReleaseInfoRequest(
        String itemCode,
        String brandName,
        String platformName,
        String platformLogoImage,
        String siteUrl,
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}", message = "yyyy-MM-dd HH:mm 형식으로 입력해주세요")
        String releaseDate,
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}", message = "yyyy-MM-dd HH:mm 형식으로 입력해주세요")
        String dueDate,
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}", message = "yyyy-MM-dd HH:mm 형식으로 입력해주세요")
        String presentationDate
) {
}
