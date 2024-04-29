package com.ward.ward_server.api.item.entity.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Category {
    CLOTHING("옷", 500),
    FOOTWEAR("신발", 501),
    ACCESSORY("악세서리", 502),
    OTHER("기타", 503);

    private String korean;
    private int code;

    public static Category ofCode(Integer dbData){
        return Arrays.stream(Category.values())
                .filter(e->e.getCode()==dbData)
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(String.format("brand code %d not exists.",dbData)));
    }

    public static Category ofKorean(String korean){
        return Arrays.stream(Category.values())
                .filter(e->e.getKorean().equals(korean))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(String.format("brand code %s not exists.",korean)));
    }
}
