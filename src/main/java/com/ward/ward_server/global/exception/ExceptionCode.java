package com.ward.ward_server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    //예시 - 삭제 예정
    INPUT_NULL(5100, "입력값이 없어요."),

    //유저 회원가입 및 로그인
    EMAIL_ALREADY_EXISTS(5201,"이미 사용 중인 이메일입니다."),
    NON_EXISTENT_EMAIL(5202, "존재하지 않는 이메일입니다"),
    INVALID_EMAIL_FORMAT(5203, "유효하지 않은 이메일 형식입니다."),
    INVALID_PASSWORD_FORMAT(5204,"유효하지 않은 비밀번호 형식입니다."),
    LOGIN_FAIL(5205,"로그인에 실패하였습니다."),
    INVALID_USERNAME_OR_PASSWORD(5206,"잘못된 아이디 또는 비밀번호입니다."),
    REGISTRATION_ERROR_MESSAGE(5207,"회원 가입 중 오류가 발생했습니다. 나중에 다시 시도해주세요.");

    private final int code;
    private final String message;
}
