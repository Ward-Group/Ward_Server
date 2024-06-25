package com.ward.ward_server.api.item.entity.enums.converter;

import com.ward.ward_server.api.item.entity.enums.BrandSort;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class BrandSortControllerAdvice {
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(BrandSort.class, new BrandSortConverter<>(BrandSort.class));
    }
}
