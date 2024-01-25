package com.ward.ward_server.webcrawling;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = KasinaWebCrawler.class)
class KasinaWebCrawlerTest {
    @Autowired
    KasinaWebCrawler crawler;

    @Test
    void test_connection() {
        List<String> datas = crawler.getData();
//        for (WebProductData data : datas) {
//            System.out.println(data.toString());
//        }
    }
}