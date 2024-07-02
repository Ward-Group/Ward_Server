package com.ward.ward_server.global.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    FILE_CONVERT_FAIL("변환할 수 없는 파일입니다."),
    DATA_TYPE_CONVERT_FAIL("데이터타입 변환에 실패했습니다."),
    NON_EXISTENT_EMAIL("존재하지 않는 이메일입니다."),
    NAME_MUST_BE_PROVIDED("한글이름 혹은 영문이름 중 하나는 반드시 제공해주세요."),
    INVALID_ENGLISH_NAME("영문이름은 영어와 숫자로만 구성해주세요."),
    INVALID_KOREAN_NAME("한글이름은 최소 한글자는 한글로 구성해주세요."),
    REQUIRED_FIELDS_MUST_BE_PROVIDED("필수항목은 반드시 제공해주세요."),
    NOTIFICATION_METHOD_NOT_EXISTS("입력하신 공지방법이 존재하지 않아요."),
    RELEASE_METHOD_NOT_EXISTS("입력하신 발매방법이 존재하지 않아요."),
    DELIVERY_METHOD_NOT_EXISTS("입력하신 배달방법이 존재하지 않아요."),
    CURRENCY_UNIT_NOT_EXISTS("입력하신 화폐단위가 존재하지 않아요."),
    SORT_NOT_EXISTS("입력하신 정렬방법이 존재하지 않아요."),
    CATEGORY_NOT_EXISTS("입력하신 카테고리가 존재하지 않아요."),
    BASIC_SORT_NOT_EXISTS("입력하신 기본 정렬이 존재하지 않아요."),
    SERVER_ERROR("서버에서 오류가 발생했습니다."),
    RESOURCE_NOT_FOUND("요청하신 페이지를 찾을 수 없어요.");

    private final String message;
}
