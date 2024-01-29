package com.ward.ward_server.webcrawling.entity.enumtype.converter;

import com.ward.ward_server.webcrawling.entity.enumtype.Brand;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class BrandConverter implements AttributeConverter<Brand, Integer> {


    @Override
    public Integer convertToDatabaseColumn(Brand brand) {
        if(brand==null) return 0;
        return brand.getCode();
    }

    @Override
    public Brand convertToEntityAttribute(Integer dbData) {
        if(dbData==0) return null;
        try{
            return Brand.ofCode(dbData);
        }catch (IllegalArgumentException e){
            log.error("failure to convert code {} -> brand ?", dbData);
            throw e;
        }
    }
}
