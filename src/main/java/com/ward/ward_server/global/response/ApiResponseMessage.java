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
    ITEM_DELETE_SUCCESS("아이템 삭제에 성공하였습니다.");

    private final String message;
}
