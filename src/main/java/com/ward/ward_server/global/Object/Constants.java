package com.ward.ward_server.global.Object;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final DateTimeFormatter DATE_STRING_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final int HOME_PAGE_SIZE = 10;
    public static final int API_PAGE_SIZE = 30;
    public static final String KAKAO = "kakao";
    public static final String APPLE = "apple";
}
