package com.ward.ward_server.api.item.entity.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Category {
    CLOTHING("옷"),
    FOOTWEAR("신발"),
    ACCESSORY("악세서리"),
    OTHER("기타");
    private String desc;

    public static Category ofText(String text){
        return Arrays.stream(Category.values())
                .filter(e -> e.toString().toLowerCase().equals(text))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("category %s not exists.", text)));
    }
}
