package com.ward.ward_server.webcrawling;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebCrawlingUtil.class)
class WebCrawlingUtilTest {

    @Autowired
    WebCrawlingUtil util;

    @Test
    void test_connection() {
        List<WebProductData> datas = util.getPage();
        for (WebProductData data : datas) {
            System.out.println(data.toString());
        }
    }
}