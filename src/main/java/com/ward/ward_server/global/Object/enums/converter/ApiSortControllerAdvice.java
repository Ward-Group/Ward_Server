package com.ward.ward_server.global.Object.enums.converter;

import com.ward.ward_server.global.Object.enums.ApiSort;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class ApiSortControllerAdvice {
    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(ApiSort.class, new ApiSortConverter<>(ApiSort.class));
    }
}
