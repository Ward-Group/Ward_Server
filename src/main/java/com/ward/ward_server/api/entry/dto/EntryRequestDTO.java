package com.ward.ward_server.api.entry.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryRequestDTO {
    private long userId;
    private long itemId;
}