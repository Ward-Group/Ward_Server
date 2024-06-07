package com.ward.ward_server.api.item.dto;

import java.util.List;

public record ItemSimpleResponse(
        String itemKoreanName,
        String itemEnglishName,
        String itemCode,
        String itemMainImage,
        String brandKoreanName,
        String brandEnglishName,
        boolean wished
) {
}
