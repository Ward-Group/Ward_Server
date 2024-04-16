package com.ward.ward_server.api.entry.dto;

import com.ward.ward_server.api.entry.domain.EntryRecord;
import com.ward.ward_server.api.item.dto.ItemDTO;
import lombok.Data;

import java.util.Date;

@Data
public class EntryRecordResponseDTO {

    private Long entryRecordId;
    private String name;
    private ItemDTO item;
    private Date entryDate;

    public EntryRecordResponseDTO(EntryRecord entryRecord) {
        this.entryRecordId = entryRecord.getEntryId();
        this.name = entryRecord.getUser().getName();
        this.item = new ItemDTO(entryRecord.getItem());
        this.entryDate = entryRecord.getEntryDate();
    }
}
