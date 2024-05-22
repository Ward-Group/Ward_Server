package com.ward.ward_server.api.entry.dto;

public record EntryRecordRequest(
        String itemCode,
        String brandName,
        String platformName,
        String memo
) {
}
