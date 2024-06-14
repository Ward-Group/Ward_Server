package com.ward.ward_server.api.releaseInfo.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.ward.ward_server.global.exception.ExceptionCode.INVALID_INPUT;
import static com.ward.ward_server.global.response.error.ErrorCode.DELIVERY_METHOD_NOT_EXISTS;

@Getter
@AllArgsConstructor
public enum DeliveryMethod {
    DOMESTIC("국내배송"),
    DIRECT("직접배송"),
    AGENCY("배달대행지");
    private String desc;

    @JsonCreator
    public static DeliveryMethod from(String text) {
        return Arrays.stream(DeliveryMethod.values())
                .filter(e -> e.toString().toLowerCase().equals(text.replace('-', '_')))
                .findAny()
                .orElseThrow(() -> new ApiException(INVALID_INPUT, DELIVERY_METHOD_NOT_EXISTS.getMessage()));
    }
}
