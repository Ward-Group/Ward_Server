package com.ward.ward_server.global.Object;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
@Getter
@Setter
public class PageResponse<T> {
    private List<T> data;
    private PageInfo pageInfo;

    public PageResponse(List<T> data, Page pages){
        this.data=data;
        this.pageInfo=new PageInfo(pages.getNumber() + 1,
                pages.getSize(), pages.getTotalElements(), pages.getTotalPages());
    }
}
