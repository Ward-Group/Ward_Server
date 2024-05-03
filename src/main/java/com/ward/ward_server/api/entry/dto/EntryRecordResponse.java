package com.ward.ward_server.api.entry.dto;

import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;

import java.util.Date;

public record EntryRecordResponse(
        String entryDate,
        String memo
) {
}
