package com.ward.ward_server.global.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    FILE_CONVERT_FAIL("변환할 수 없는 파일입니다."),
    DATA_TYPE_CONVERT_FAIL("데이터타입 변환에 실패했습니다."),
    NON_EXISTENT_EMAIL("존재하지 않는 이메일"),
    NAME_MUST_BE_PROVIDED("한글이름 혹은 영문이름 중 하나는 반드시 제공해주세요."),
    INVALID_ENGLISH_NAME("영문이름은 영어와 숫자로만 구성해주세요."),
    INVALID_KOREAN_NAME("한글이름은 최소 한글자는 한글로 구성해주세요."),
    REQUIRED_FIELDS_MUST_BE_PROVIDED("필수항목은 반드시 제공해주세요."),
    ITEM_IDENTIFY_BY_ITEM_CODE_AND_BRAND_NAME("상품은 상품코드와 브랜드이름으로 식별할 수 있습니다. 둘 다 입력해주세요."),
    NOTIFICATION_METHOD_NOT_EXISTS("입력하신 공지방법이 존재하지 않아요."),
    RELEASE_METHOD_NOT_EXISTS("입력하신 발매방법이 존재하지 않아요."),
    DELIVERY_METHOD_NOT_EXISTS("입력하신 배달방법이 존재하지 않아요."),
    CURRENCY_UNIT_NOT_EXISTS("입력하신 화폐단위가 존재하지 않아요."),
    SORT_NOT_EXISTS("입력하신 정렬방법이 존재하지 않아요."),
    CATEGORY_NOT_EXISTS("입력하신 카테고리가 존재하지 않아요."),
    BRAND_SORT_NOT_EXISTS("입력하신 브랜드 정렬이 존재하지 않아요."),
    SERVER_ERROR("서버에서 오류가 발생했습니다.");

    private final String message;
}
