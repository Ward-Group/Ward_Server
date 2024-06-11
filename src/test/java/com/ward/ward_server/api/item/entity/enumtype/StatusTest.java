package com.ward.ward_server.api.item.entity.enumtype;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

class StatusTest {

    @Test
    public void isToday() {
        LocalDateTime 지금 = LocalDateTime.now();
        LocalDateTime 대과거 = LocalDateTime.parse("2023-04-10T15:30:00");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        LocalDateTime 과거 = LocalDateTime.parse("2024-05-15 05:30:00.000000", formatter);
        LocalDateTime 한달후 = LocalDateTime.of(2024, 7, 3, 12, 30, 30);
        LocalDateTime 오늘날짜만동일 = LocalDateTime.of(2024, 6, 3, 12, 30, 30);
        System.out.println("지금 : " + 지금);
        System.out.println("대과거 : " + 대과거);
        System.out.println("과거 : " + 과거);
        System.out.println("한달후 : " + 한달후);
        System.out.println("오늘날짜만동일 : " + 오늘날짜만동일);

        //duration 시간 단위 비교
        Duration duration = Duration.between(지금, 오늘날짜만동일);
        System.out.println("duration -> seconds :" + duration.getSeconds());

        //period 날짜 단위 비교
        Period period = Period.between(지금.toLocalDate(), 오늘날짜만동일.toLocalDate());
        System.out.println("period -> days :" + period.getDays());

        //ChronoUnit
        long cu_day = ChronoUnit.DAYS.between(지금, 오늘날짜만동일);
        System.out.println("cu day:"+cu_day);
        long cu_hour = ChronoUnit.HOURS.between(지금, 오늘날짜만동일);
        System.out.println("cu_hour:"+cu_hour);

        //after

    }

}