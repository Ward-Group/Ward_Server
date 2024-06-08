package com.ward.ward_server.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    //공통
    INVALID_INPUT(5000, "부적절한 입력입니다."),
    SERVER_ERROR(5001, "서버에서 오류가 발생했습니다."),

    //유저 회원가입 및 로그인
    NON_EXISTENT_EMAIL(5201, "존재하지 않는 이메일입니다"),
    INVALID_EMAIL_FORMAT(5202, "유효하지 않은 이메일 형식입니다."),
    INVALID_USERNAME_OR_PASSWORD(5203, "잘못된 아이디 또는 비밀번호입니다."),
    REGISTRATION_ERROR_MESSAGE(5204, "회원 가입 중 오류가 발생했습니다. 나중에 다시 시도해주세요."),
    USER_NOT_FOUND(5205, "사용자를 찾을 수 없습니다."),
    DUPLICATE_NICKNAME(5206, "이미 사용 중인 닉네임입니다."),
    INVALID_REFRESH_TOKEN(5207, "유효하지 않은 리프레시 토큰입니다."),
    NON_EXISTENT_USER(5208, "존재하지 않는 회원입니다. 회원가입이 필요합니다."),
    EXISTENT_USER(5209,"해당 이메일은 다른 소셜 로그인으로 가입한 내역이 존재합니다. 계정 통합 의사를 확인 후 기존 소셜로그인 진행 후 통합합니다."),
    EXISTENT_USER_AT_REGISTER(5210,"해당 이메일로 가입한 내역이 존재합니다. 가입한 이메일로 로그인을 진행해야합니다."),
    EXISTENT_USER_AT_REGISTER_WITH_PROVIDER_PID(5211,"해당 provider+providerId에 해당하는 회원이 존재합니다. 로그인을 진행해야합니다."),
    EXISTENT_USER_AT_ADD_SOCIAL_ACCOUNT(5212,"해당 provider+providerId에 해당하는 회원이 존재합니다. 기존 연동된 계정에서 해제 후 다시 연동 시도바랍니다."),
    TOKEN_REFRESH_FAILURE(5213, "토큰 갱신에 실패하였습니다."),

    //아이템
    ITEM_NOT_FOUND(5300, "상품을 찾을 수 없습니다."),
    DUPLICATE_ITEM(5309, "이미 등록된 상품입니다."),

    BRAND_NOT_FOUND(5350, "브랜드를 찾을 수 없습니다."),
    DUPLICATE_BRAND(5351, "이미 등록된 브랜드입니다."),

    //발매정보
    RELEASE_INFO_NOT_FOUND(5400, "발매정보를 찾을 수 없습니다."),
    DUPLICATE_RELEASE_INFO(5401, "이미 등록된 발매정보입니다."),
    DUPLICATE_DRAW_PLATFORM_NAME(5450, "이미 등록된 발매플랫폼명입니다."),
    DRAW_PLATFORM_NOT_FOUND(5451, "발매플랫폼을 찾을 수 없습니다."),
    RELEASE_INFO_EXPIRED(5452, "만료된 발매정보입니다."),

    //응모
    DUPLICATE_ENTRY_RECORD(5500, "이미 응모하였습니다."),
    ENTRY_RECORD_NOT_FOUND(5501, "응모내역이 존재하지 않습니다."),

    //관심상품
    WISH_ITEM_NOT_FOUND(5600, "관심상품을 찾을 수 없습니다."),
    DUPLICATE_WISH_ITEM(5600, "이미 등록한 관심상품입니다."),

    //관심브랜드
    WISH_BRAND_NOT_FOUND(5700, "관심브랜드를 찾을 수 없습니다."),
    DUPLICATE_WISH_BRAND(5701, "이미 등록한 관심브랜드입니다.");

    private final int code;
    private final String message;
}
