package com.ward.ward_server.global.Object.enums.converter;

import com.ward.ward_server.global.Object.enums.ApiSort;
import org.springframework.core.convert.converter.Converter;

public class ApiSortConverter implements Converter<String, ApiSort> {
    @Override
    public ApiSort convert(String text) {
        return ApiSort.from(text);
    }
}
