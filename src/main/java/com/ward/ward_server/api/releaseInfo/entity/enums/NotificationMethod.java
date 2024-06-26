package com.ward.ward_server.api.releaseInfo.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ward.ward_server.global.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.ward.ward_server.global.exception.ExceptionCode.INVALID_INPUT;
import static com.ward.ward_server.global.response.error.ErrorMessage.NOTIFICATION_METHOD_NOT_EXISTS;

@Getter
@AllArgsConstructor
public enum NotificationMethod {
    EMAIL("이메일 알림"),
    APP_NOTIFICATION("APP 알림");
    private String desc;

    @JsonCreator
    public static NotificationMethod from(String text) {
        return Arrays.stream(NotificationMethod.values())
                .filter(e -> e.toString().equals(text.replace('-', '_').toUpperCase()))
                .findAny()
                .orElseThrow(() -> new ApiException(INVALID_INPUT, NOTIFICATION_METHOD_NOT_EXISTS.getMessage()));
    }
}
