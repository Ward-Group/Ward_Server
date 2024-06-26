package com.ward.ward_server.global.Object.enums.converter;

import com.ward.ward_server.global.Object.enums.ApiSort;

import java.beans.PropertyEditorSupport;

public class ApiSortConverter<T extends Enum<T>> extends PropertyEditorSupport {
    private final Class<T> type;

    public ApiSortConverter(Class<T> type){
        this.type=type;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(ApiSort.from(text));
    }
}
