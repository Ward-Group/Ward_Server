package com.ward.ward_server.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CrawlerConfig {

    @Value("${crawler.chromeDriverPath}")
    private String chromeDriverPath;

    public String getChromeDriverPath() {
        return chromeDriverPath;
    }
}
