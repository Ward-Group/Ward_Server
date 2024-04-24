package com.ward.ward_server.global.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    FILE_CONVERT_FAIL(500, "변환할 수 없는 파일입니다."),
    DATA_TYPE_CONVERT_FAIL(500, "데이터타입 변환에 실패했습니다."),
    NON_EXISTENT_EMAIL(401, "존재하지 않는 이메일");
    private final int code;
    private final String message;
}
