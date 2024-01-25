package com.ward.ward_server.webcrawling;

import com.ward.ward_server.webcrawling.entity.Item;
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
    NikeWebCrawler util;

    @Test
    void test_connection() {
        List<Item> datas = util.getData();
        for (Item data : datas) {
            System.out.println(data.toString());
        }
    }
}