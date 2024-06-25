package com.ward.ward_server.global.Object.enums;

import com.ward.ward_server.global.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.ward.ward_server.global.exception.ExceptionCode.INVALID_INPUT;
import static com.ward.ward_server.global.response.error.ErrorCode.BRAND_SORT_NOT_EXISTS;

@Getter
@AllArgsConstructor
public enum ApiSort {
    KOREAN_ALPHABETICAL("한글순"),
    ALPHABETICAL("영문순"),
    RANKING("랭킹순");

    private String desc;

    public static ApiSort from(String text) {
        return Arrays.stream(ApiSort.values())
                .filter(e -> e.toString().equals(text.replace('-', '_').toUpperCase()))
                .findAny()
                .orElseThrow(() -> new ApiException(INVALID_INPUT, BRAND_SORT_NOT_EXISTS.getMessage()));
    }
}
