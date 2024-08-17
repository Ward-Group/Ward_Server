package com.ward.ward_server.api.item.webCrawler;

import com.ward.ward_server.api.item.dto.WebProductData;
import com.ward.ward_server.global.config.CrawlerConfig;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

//@Component
@Slf4j
public class NikeWebCrawler {

    private WebDriver driver;

    @Autowired
    public NikeWebCrawler(CrawlerConfig crawlerConfig) {
        String chromeDriverPath = crawlerConfig.getChromeDriverPath();
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.setBinary("/usr/bin/google-chrome"); // EC2 쓸 때 해제
        chromeOptions.addArguments("--headless"); //헤드리스 모드로 실행, 실제 창이 표시되지 않는다.
        chromeOptions.addArguments("--lang=ko"); //브라우저 언어를 한국어로 설정
        chromeOptions.addArguments("--no-sandbox"); //샌드박스 모드 비활성화
        chromeOptions.addArguments("--disable-dev-shm-usage"); // /dev/shm 사용 비활성화
        chromeOptions.addArguments("--disable-gpu"); //GPU 가속 비활성화
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        this.driver = new ChromeDriver(chromeOptions);
    }

    public List<WebProductData> getData() {
        String url = "https://www.nike.com/kr/launch?s=upcoming"; //나이키(Nike) SNKRS 런치 캘린더 KR
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        String html = driver.getPageSource();
        Document document = Jsoup.parse(html);

        Elements productCards = document.select(".d-md-h.ncss-col-sm-12.va-sm-t.pb0-sm.prl0-sm");
        ArrayList<WebProductData> results = new ArrayList<>();
        for (Element productCard : productCards) {

            // 1.제품명 추출
            String productName = productCard.select(".d-sm-tc .headline-5").text();

            // 2.이미지 URL 추출
            String imageUrl = productCard.select(".card-link img.image-component").attr("src");

            // 3.사이트 URL 추출
            String siteUrl = null;

            // 4.날짜 추출
            String[] dates = productCard.select(".d-sm-tc .available-date-component").text().split("[\\.\\s]+");
            int month = Integer.parseInt(dates[0]);
            int day = Integer.parseInt(dates[1]);
            String ampm = dates[2];
            int[] time = Arrays.stream(dates[3].replace("출시", "").split(":"))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            if (ampm.equals("오후")) time[0] = time[0] + 12;
            // 4-1.
            LocalDateTime releaseDate = LocalDateTime.of(2024, month, day, time[0], time[1]);

            // 4-2.마감날짜
            LocalDateTime dueDate = null;

            // 4-3.발표날짜
            LocalDateTime presentationDate = null;

            // 5.상태
            //LocalDateTime now = LocalDateTime.now();
            //Status status = Status.IMPOSSIBLE;
            //if (now.isAfter(releaseDate)) status = Status.POSSIBLE;

            // 6.브랜드 NIKE

            // 결과 저장
//            results.add(new WebProductData(productName, imageUrl, siteUrl,
//                    releaseDate, dueDate, presentationDate,
//                    status, Brand.NIKE));
        }
        return results;
    }


}
