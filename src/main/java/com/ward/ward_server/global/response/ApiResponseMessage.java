package com.ward.ward_server.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiResponseMessage {
    //유저 회원가입 및 로그인
    SIGNUP_SUCCESS("회원가입에 성공하였습니다."),
    LOGIN_SUCCESS("로그인에 성공하였습니다."),
    ADD_SOCIALLOGIN_SUCCESS("소셜 로그인 추가에 성공하였습니다."),
    UPDATE_SOCIALLOGIN_SUCCESS("소셜 로그인 변경에 성공하였습니다."),
    TOKEN_REFRESH_SUCCESS("토큰 갱신에 성공하였습니다."),
    NICKNAME_CHECK_SUCCESS("닉네임 중복 체크에 성공하였습니다."),
    UPDATE_NICKNAME_SUCCESS("닉네임 수정에 성공하였습니다."),
    LOGOUT_SUCCESS("로그아웃에 성공하였습니다."),
    //웹 크롤러
    WEBCRAWLING_NIKE_SUCCESS("나이키 데이터 로드에 성공하였습니다."),
    WEBCRAWLING_KASINA_SUCCESS("카시나 데이터 로드에 성공하였습니다."),
    //아이템
    ITEM_CREATE_SUCCESS("상품 등록에 성공하였습니다."),
    ITEM_DETAIL_LOAD_SUCCESS("상품 상세 정보 조회에 성공하였습니다."),
    ITEM_UPDATE_SUCCESS("상품 수정에 성공하였습니다."),
    ITEM_DELETE_SUCCESS("상품 삭제에 성공하였습니다."),
    ITEM_LIST_LOAD_SUCCESS("상품 목록 조회에 성공하였습니다."),
    ITEM_PAGE_LOAD_SUCCESS("상품 페이지 조회에 성공하였습니다."),
    //브랜드
    BRAND_CREATE_SUCCESS("브랜드 등록에 성공하였습니다."),
    BRAND_TOP10_WITH_ITEM_LOAD_SUCCESS("top10 브랜드와 상품 목록 조회에 성공하였습니다."),
    BRAND_RECOMMENDED_LOAD_SUCCESS("추천 브랜드 조회에 성공하였습니다."),
    BRAND_UPDATE_SUCCESS("브랜드 수정에 성공하였습니다."),
    BRAND_DELETE_SUCCESS("브랜드 삭제에 성공하였습니다."),
    BRAND_VIEW_COUNT_UP_SUCCESS("브랜드 조회수 증가에 성공하였습니다."),
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
    ENTRY_RECORD_BY_USER_LOAD_SUCCESS("회원의 발매정보와 응모 조회에 성공하였습니다."),
    //관심상품
    WISH_ITEM_CREATE_SUCCESS("관심상품 등록에 성공하였습니다"),
    WISH_ITEM_LOAD_SUCCESS("회원의 관심상품 목록 조회에 성공하였습니다"),
    WISH_ITEM_DELETE_SUCCESS("관심상품 삭제에 성공하였습니다."),
    //관심브랜드
    WISH_BRAND_CREATE_SUCCESS("관심브랜드 등록에 성공하였습니다."),
    WISH_BRAND_LOAD_SUCCESS("회원의 관심브랜드 목록 조회에 성공하였습니다."),
    WISH_BRAND_DELETE_SUCCESS("관심브랜드 삭제에 성공하였습니다.");
    private final String message;
}
