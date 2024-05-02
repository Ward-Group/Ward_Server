package com.ward.ward_server.api.entry.dto;

import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;

public record EntryRecordDetailResponse(
        boolean isEntry,
        String entryDate,
        String memo,
        ReleaseInfoSimpleResponse simpleReleaseInfo) {
}
