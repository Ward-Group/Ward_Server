package com.ward.ward_server.global.Object.enums.converter;

import com.ward.ward_server.global.Object.enums.Sort;
import org.springframework.core.convert.converter.Converter;


public class SortConverter implements Converter<String, Sort> {
    @Override
    public Sort convert(String text) {
        return Sort.from(text);
    }
}
