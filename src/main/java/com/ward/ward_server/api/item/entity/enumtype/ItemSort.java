package com.ward.ward_server.api.item.entity.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ItemSort {
    DUE_TODAY("오늘 마감 예정인 상품"),
    RELEASE_TODAY("오늘 발매한 상품"),
    WISH_RELEASE("현재 발매중인 관심상품"),
    CONFIRM_RELEASE("발매 날짜는 확정됐지만 발매전 상품"),
    REGISTER_TODAY("오늘 등록한 상품");
    private final String desc;

    public static ItemSort ofText(String text) {
        return Arrays.stream(ItemSort.values())
                .filter(e -> e.toString().toLowerCase().equals(text))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("item-sort %s not exists.", text)));
    }
}
