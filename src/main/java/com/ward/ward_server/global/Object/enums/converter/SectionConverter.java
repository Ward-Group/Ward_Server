package com.ward.ward_server.global.Object.enums.converter;

import com.ward.ward_server.global.Object.enums.Section;
import org.springframework.core.convert.converter.Converter;


public class SectionConverter implements Converter<String, Section> {
    @Override
    public Section convert(String text) {
        return Section.from(text);
    }
}
