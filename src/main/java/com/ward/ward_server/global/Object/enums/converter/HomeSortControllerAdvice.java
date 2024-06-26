package com.ward.ward_server.global.Object.enums.converter;

import com.ward.ward_server.global.Object.enums.ApiSort;
import com.ward.ward_server.global.Object.enums.HomeSort;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class HomeSortControllerAdvice {
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(HomeSort.class, new HomeSortConverter<>(HomeSort.class));
    }
}
