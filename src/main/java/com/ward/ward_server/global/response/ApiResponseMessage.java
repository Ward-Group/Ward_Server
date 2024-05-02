package com.ward.ward_server.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiResponseMessage {
    //유저 회원가입 및 로그인
    SIGNUP_SUCCESS("회원가입에 성공하였습니다."),
    LOGIN_SUCCESS("로그인에 성공하였습니다."),
    //웹 크롤러
    WEBCRAWLING_NIKE_SUCCESS("나이키 데이터 로드에 성공하였습니다."),
    WEBCRAWLING_KASINA_SUCCESS("카시나 데이터 로드에 성공하였습니다."),
    //아이템
    ITEM_CREATE_SUCCESS("아이템 등록에 성공하였습니다."),
    ITEM_DETAIL_LOAD_SUCCESS("아이템 상세 정보 조회에 성공하였습니다."),
    ITEM_LIST_LOAD_SUCCESS("아이템 목록 조회에 성공하였습니다."),
    ITEM_UPDATE_SUCCESS("아이템 수정에 성공하였습니다."),
    ITEM_DELETE_SUCCESS("아이템 삭제에 성공하였습니다."),
    //발매정보
    RELEASE_INFO_CREATE_SUCCESS("발매정보 등록에 성공하였습니다."),
    RELEASE_INFO_LIST_LOAD_SUCCESS("발매정보 목록 조회에 성공하였습니다."),
    RELEASE_INFO_UPDATE_SUCCESS("발매정보 수정에 성공하였습니다."),
    RELEASE_INFO_DELETE_SUCCESS("발매정보를 삭제했습니다."),
    DRAW_PLATFORM_CREATE_SUCCESS("발매플랫폼 등록에 성공하였습니다."),
    DRAW_PLATFORM_UPDATE_SUCCESS("발매플랫폼 수정에 성공하였습니다."),
    DRAW_PLATFORM_DELETE_SUCCESS("발매플랫폼 삭제에 성공하였습니다."),
    //응모
    ENTRY_RECORD_CREATE_SUCCESS("응모 등록에 성공하였습니다."),
    ENTRY_RECORD_DELETE_SUCCESS("응모 삭제에 성공하였습니다."),
    ENTRY_RECORD_BY_ITEM_LOAD_SUCCESS("상품의 발매정보와 응모 조회에 성공하였습니다."),
    ENTRY_RECORD_BY_USER_LOAD_SUCCESS("회원의 발매정보와 응모 조회에 성공하였습니다.");
    private final String message;
}
