package com.ward.ward_server.api.releaseInfo.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ward.ward_server.global.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.ward.ward_server.global.exception.ExceptionCode.INVALID_INPUT;
import static com.ward.ward_server.global.response.error.ErrorMessage.RELEASE_METHOD_NOT_EXISTS;

@Getter
@AllArgsConstructor
public enum ReleaseMethod {
    ENTRY("응모"),
    FIRST_COME("선착순");
    private String desc;

    @JsonCreator
    public static ReleaseMethod from(String text) {
        return Arrays.stream(ReleaseMethod.values())
                .filter(e -> e.toString().equals(text.replace('-', '_').toUpperCase()))
                .findAny()
                .orElseThrow(() -> new ApiException(INVALID_INPUT, RELEASE_METHOD_NOT_EXISTS.getMessage()));
    }
}
