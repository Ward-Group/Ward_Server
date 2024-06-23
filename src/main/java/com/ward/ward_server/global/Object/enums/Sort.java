package com.ward.ward_server.global.Object.enums;

import com.ward.ward_server.api.releaseInfo.entity.enums.CurrencyUnit;
import com.ward.ward_server.global.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.ward.ward_server.global.exception.ExceptionCode.INVALID_INPUT;
import static com.ward.ward_server.global.response.error.ErrorCode.CURRENCY_UNIT_NOT_EXISTS;
import static com.ward.ward_server.global.response.error.ErrorCode.SORT_NOT_EXISTS;

@Getter
@AllArgsConstructor
public enum Sort {
    DUE_TODAY("오늘 마감예정인 상품/발매정보"),
    RELEASE_NOW("현재 발매중인 상품/발매정보"),
    RELEASE_WISH("현재 발매중인 관심상품"),
    RELEASE_CONFIRM("발매예정인 상품/발매정보"),
    REGISTER_TODAY("오늘 등록한 상품/발매정보");
    private final String desc;

    public static Sort from(String text) {
        return Arrays.stream(Sort.values())
                .filter(e -> e.toString().equals(text.replace('-', '_').toUpperCase()))
                .findAny()
                .orElseThrow(() -> new ApiException(INVALID_INPUT, SORT_NOT_EXISTS.getMessage()));
    }
}
