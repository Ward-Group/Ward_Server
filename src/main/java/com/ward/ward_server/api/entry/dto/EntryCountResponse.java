package com.ward.ward_server.api.entry.dto;

public record EntryCountResponse(
        long total,
        long inProgress, // 진행
        long announcement, // 당첨자 발표
        long closed // 종료
) {
}
