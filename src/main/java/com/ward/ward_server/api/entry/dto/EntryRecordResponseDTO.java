package com.ward.ward_server.api.entry.dto;

import com.ward.ward_server.api.entry.domain.EntryRecord;
//import com.ward.ward_server.api.item.dto.ItemDto;
import lombok.Data;

import java.util.Date;

@Data
public class EntryRecordResponseDTO {

    private Long entryRecordId;
    private String name;
    //private ItemDto item;
    private Date entryDate;

    public EntryRecordResponseDTO(EntryRecord entryRecord) {
        this.entryRecordId = entryRecord.getId();
        this.name = entryRecord.getUser().getName();
        //this.item = new ItemDTO(entryRecord.getItem());
        //FIXME 잠시 주석처리
        this.entryDate = entryRecord.getEntryDate();
    }
}
