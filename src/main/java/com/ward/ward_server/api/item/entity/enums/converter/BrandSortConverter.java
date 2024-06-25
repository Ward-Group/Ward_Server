package com.ward.ward_server.api.item.entity.enums.converter;

import com.ward.ward_server.api.item.entity.enums.BrandSort;

import java.beans.PropertyEditorSupport;

public class BrandSortConverter<T extends Enum<T>> extends PropertyEditorSupport {
    private final Class<T> type;

    public BrandSortConverter(Class<T> type){
        this.type=type;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(BrandSort.from(text));
    }
}
