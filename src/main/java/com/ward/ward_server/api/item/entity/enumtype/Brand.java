package com.ward.ward_server.api.item.entity.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Brand {
    NIKE("나이키", 100),
    KASINA("카시나", 101);

    private String koreanName;
    private int code;

    public static Brand ofCode(Integer dbData){
        return Arrays.stream(Brand.values())
                .filter(e->e.getCode()==dbData)
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(String.format("brand code %d not exists.",dbData)));
    }
}
