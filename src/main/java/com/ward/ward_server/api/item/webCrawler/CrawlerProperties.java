package com.ward.ward_server.api.item.webCrawler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@Setter
@ConfigurationProperties(prefix = "crawler")
public class CrawlerProperties {
    private String chromeDriverPath;
}
