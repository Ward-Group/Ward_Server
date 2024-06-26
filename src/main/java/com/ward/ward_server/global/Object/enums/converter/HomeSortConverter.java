package com.ward.ward_server.global.Object.enums.converter;

import com.ward.ward_server.global.Object.enums.ApiSort;
import com.ward.ward_server.global.Object.enums.HomeSort;
import org.springframework.core.convert.converter.Converter;

import java.beans.PropertyEditorSupport;


public class HomeSortConverter <T extends Enum<T>> extends PropertyEditorSupport {
    private final Class<T> type;

    public HomeSortConverter(Class<T> type){
        this.type=type;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(HomeSort.from(text));
    }
}
