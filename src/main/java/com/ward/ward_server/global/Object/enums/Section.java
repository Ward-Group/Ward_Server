package com.ward.ward_server.global.Object.enums;

import com.ward.ward_server.global.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.ward.ward_server.global.exception.ExceptionCode.INVALID_INPUT;
import static com.ward.ward_server.global.response.error.ErrorMessage.SORT_NOT_EXISTS;

@Getter
@AllArgsConstructor
public enum Section {
    DUE_TODAY("오늘 마감예정"),
    RELEASE_NOW("현재 발매중"),
    RELEASE_WISH("관심상품 중 현재 발매중"),
    RELEASE_SCHEDULE("발매 예정"),
    REGISTER_TODAY("오늘 등록"),
    END("발매 종료");
    private final String desc;

    public static Section from(String text) {
        return Arrays.stream(Section.values())
                .filter(e -> e.toString().equals(text.replace('-', '_').toUpperCase()))
                .findAny()
                .orElseThrow(() -> new ApiException(INVALID_INPUT, SORT_NOT_EXISTS.getMessage()));
    }
}
