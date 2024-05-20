package com.ward.ward_server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    //공통
    INVALID_INPUT(5000, "부적절한 입력입니다."),

    //유저 회원가입 및 로그인
    EMAIL_ALREADY_EXISTS(5201, "이미 사용 중인 이메일입니다."),
    NON_EXISTENT_EMAIL(5202, "존재하지 않는 이메일입니다"),
    INVALID_EMAIL_FORMAT(5203, "유효하지 않은 이메일 형식입니다."),
    INVALID_PASSWORD_FORMAT(5204, "유효하지 않은 비밀번호 형식입니다."),
    LOGIN_FAIL(5205, "로그인에 실패하였습니다."),
    INVALID_USERNAME_OR_PASSWORD(5206, "잘못된 아이디 또는 비밀번호입니다."),
    REGISTRATION_ERROR_MESSAGE(5207, "회원 가입 중 오류가 발생했습니다. 나중에 다시 시도해주세요."),
    USER_NOT_FOUND(5208, "사용자를 찾을 수 없습니다."),
    DUPLICATE_WISHLIST(5211, "이미 등록하였습니다."),
    NO_WISHLIST_FOUND(5212, "관심 목록이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(5213, "유효하지 않은 리프레시 토큰입니다."),

    //아이템
    ITEM_NOT_FOUND(5300, "상품을 찾을 수 없습니다."),
    DUPLICATE_ITEM_CODE(5309, "이미 등록된 상품코드입니다."),

    //발매정보
    RELEASE_INFO_NOT_FOUND(5400, "발매정보를 찾을 수 없습니다."),
    DUPLICATE_RELEASE_INFO(5401, "이미 등록된 발매정보입니다."),
    DUPLICATE_DRAW_PLATFORM_NAME(5450, "이미 등록된 발매플랫폼명입니다."),
    DRAW_PLATFORM_NOT_FOUND(5451, "발매플랫폼을 찾을 수 없습니다."),
    RELEASE_INFO_EXPIRED(5452, "만료된 발매정보입니다."),

    //응모
    DUPLICATE_ENTRY_RECORD(5500, "이미 응모하였습니다."),
    ENTRY_RECORD_NOT_FOUND(5501, "응모내역이 존재하지 않습니다.");

    private final int code;
    private final String message;
}
