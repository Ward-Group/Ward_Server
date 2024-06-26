package com.ward.ward_server.api.releaseInfo.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ward.ward_server.global.exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.ward.ward_server.global.exception.ExceptionCode.INVALID_INPUT;
import static com.ward.ward_server.global.response.error.ErrorMessage.CURRENCY_UNIT_NOT_EXISTS;

@Getter
@AllArgsConstructor
public enum CurrencyUnit {
    KRW("대한민국", "South Korea", "원", "Won", "₩"),
    USD("미국", "United States", "달러", "Dollar", "$"),
    EUR("유럽", "European Union", "유로", "Euro", "€"),
    JPY("일본", "Japan", "엔", "Yen", "¥"),
    CNY("중국", "China", "위안", "Yuan", "¥");
    private String countryKoreanName;
    private String countryEnglishName;
    private String currencyKoreanName;
    private String currencyEnglishName;
    private String symbol;

    @JsonCreator
    public static CurrencyUnit from(String text) {
        return Arrays.stream(CurrencyUnit.values())
                .filter(e -> e.toString().equals(text.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new ApiException(INVALID_INPUT, CURRENCY_UNIT_NOT_EXISTS.getMessage()));
    }
}
