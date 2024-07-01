package com.ward.ward_server.global.Object.enums.converter;

import com.ward.ward_server.global.Object.enums.BasicSort;
import org.springframework.core.convert.converter.Converter;

public class BasicSortConverter implements Converter<String, BasicSort> {
    @Override
    public BasicSort convert(String text) {
        return BasicSort.from(text);
    }
}
