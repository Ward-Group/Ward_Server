package com.ward.ward_server.global.config;

import com.ward.ward_server.api.item.entity.enums.converter.CategoryConverter;
import com.ward.ward_server.global.Object.enums.converter.ApiSortConverter;
import com.ward.ward_server.global.Object.enums.converter.HomeSortConverter;
import org.checkerframework.checker.units.qual.A;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new CategoryConverter());
        registry.addConverter(new ApiSortConverter());
        registry.addConverter(new HomeSortConverter());
    }
}
