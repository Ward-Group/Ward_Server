package com.ward.ward_server.global.Object.enums.converter;

import com.ward.ward_server.global.Object.enums.HomeSort;
import org.springframework.core.convert.converter.Converter;


public class SortConverter implements Converter<String, HomeSort> {
    @Override
    public HomeSort convert(String text) {
        return HomeSort.from(text);
    }
}
