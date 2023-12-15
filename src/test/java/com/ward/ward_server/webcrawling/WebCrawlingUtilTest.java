package com.ward.ward_server.webcrawling;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = WebCrawlingUtil.class)
class WebCrawlingUtilTest {

    @Autowired
    WebCrawlingUtil util;

    @Test
    void test_connection(){
        List<WebProductData> datas=util.getPage();
        for(WebProductData data:datas){
            System.out.println(data.toString());
        }
    }

}