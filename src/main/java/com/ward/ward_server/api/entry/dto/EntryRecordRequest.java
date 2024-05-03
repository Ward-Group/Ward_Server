package com.ward.ward_server.api.entry.dto;

public record EntryRecordRequest(
        String itemCode,
        String platformName,
        String memo
) {
}
