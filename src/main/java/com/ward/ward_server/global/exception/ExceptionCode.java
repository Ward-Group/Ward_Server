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
    REGISTRATION_ERROR_MESSAGE(5207,"회원 가입 중 오류가 발생했습니다. 나중에 다시 시도해주세요."),
    USER_NOT_FOUND(5208, "사용자를 찾을 수 없습니다."),
    ITEM_NOT_FOUND(5208, "아이템을 찾을 수 없습니다."),
    DUPLICATE_ENTRY(5209, "이미 응모하였습니다."),
    NO_ENTRY_RECORD_FOUND(5210, "응모내역이 존재하지 않습니다."),
    DUPLICATE_WISHLIST(5211, "이미 등록하였습니다."),
    NO_WISHLIST_FOUND(5212, "관심 목록이 존재하지 않습니다.");


    private final int code;
    private final String message;
}
