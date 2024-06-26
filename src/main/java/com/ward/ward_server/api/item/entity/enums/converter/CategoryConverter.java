package com.ward.ward_server.api.item.entity.enums.converter;

import com.ward.ward_server.api.item.entity.enums.Category;
import org.springframework.core.convert.converter.Converter;

public class CategoryConverter implements Converter<String, Category> {
    @Override
    public Category convert(String text) {
        return Category.from(text);
    }
}
