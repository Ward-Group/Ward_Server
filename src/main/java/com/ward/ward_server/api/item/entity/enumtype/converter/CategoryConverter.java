package com.ward.ward_server.api.item.entity.enumtype.converter;

import com.ward.ward_server.api.item.entity.enumtype.Category;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class CategoryConverter implements AttributeConverter<Category, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Category category) {
        if(category==null) return 0;
        return category.getCode();
    }

    @Override
    public Category convertToEntityAttribute(Integer dbData) {
        if(dbData==0) return null;
        try{
            return Category.ofCode(dbData);
        }catch (IllegalArgumentException e){
            log.error("failure to convert code {} -> category ?", dbData);
            throw e;
        }
    }
}
