package com.ward.ward_server.api.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginErrorResponse {
    // TODO 다 record 로 변경하기
    private final int status;
    private final boolean success;
    private final String message;
    private final String error;
}
