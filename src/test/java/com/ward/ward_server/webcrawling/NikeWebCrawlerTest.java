package com.ward.ward_server.webcrawling;

import com.ward.ward_server.api.webcrawling.dto.WebProductData;
import com.ward.ward_server.api.webcrawling.webCrawler.NikeWebCrawler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = NikeWebCrawler.class)
class NikeWebCrawlerTest {

    @Autowired
    NikeWebCrawler crawler;

    @Test
    void test_connection() {
        List<WebProductData> datas = crawler.getData();
        for (WebProductData data : datas) {
            System.out.println(data.toString());
        }
    }
}