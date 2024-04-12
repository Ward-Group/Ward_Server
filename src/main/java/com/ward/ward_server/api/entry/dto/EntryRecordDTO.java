package com.ward.ward_server.api.entry.dto;

import com.ward.ward_server.api.entry.domain.EntryRecord;
import com.ward.ward_server.api.item.dto.ItemDTO;
import lombok.Data;

import java.util.Date;

@Data
public class EntryRecordDTO {

    private Long entryRecordId;
    private String name;
    private ItemDTO item;
    private Date entryDate;

    public EntryRecordDTO(EntryRecord entryRecord) {
        this.entryRecordId = entryRecord.getEntryId();
        this.name = entryRecord.getUser().getName();
        this.entryDate = entryRecord.getEntryDate();
        this.item = new ItemDTO(entryRecord.getItem());
    }
}
