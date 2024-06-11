package com.ward.ward_server.api.item.entity.enumtype.converter;

import com.ward.ward_server.api.item.entity.enumtype.ItemSort;
import org.springframework.core.convert.converter.Converter;


public class ItemSortConverter implements Converter<String, ItemSort> {
    @Override
    public ItemSort convert(String text) {
        return ItemSort.ofText(text);
    }
}
