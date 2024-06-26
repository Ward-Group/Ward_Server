package com.ward.ward_server.api.item.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ward.ward_server.global.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.ward.ward_server.global.exception.ExceptionCode.INVALID_INPUT;
import static com.ward.ward_server.global.response.error.ErrorMessage.CATEGORY_NOT_EXISTS;

@Getter
@AllArgsConstructor
public enum Category {
    CLOTHING("의류"),
    FOOTWEAR("신발"),
    ACCESSORY("악세서리"),
    OTHER("기타");
    private String desc;
    @JsonCreator
    public static Category from(String text) {
        return Arrays.stream(Category.values())
                .filter(e -> e.toString().equals(text.replace('-', '_').toUpperCase()))
                .findAny()
                .orElseThrow(() -> new ApiException(INVALID_INPUT, CATEGORY_NOT_EXISTS.getMessage()));
    }
}
