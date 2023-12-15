package com.ward.ward_server.webcrawling;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class WebCrawlingUtil {
    //FIXME 나중에 사이트별로 웹 크롤러 만들때 다 쓰이니까 전역변수로 만들어줘야 한다.
    private String CHROME_DRIVER_PATH = "D:\\chromedriver/chromedriver-win64/chromedriver.exe";

    private WebDriver driver;

    @Autowired
    public WebCrawlingUtil() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless=new"); //헤드리스 모드로 실행, 실제 창이 표시되지 않는다.
        chromeOptions.addArguments("--lang=ko"); //브라우저 언어를 한국어로 설정
        chromeOptions.addArguments("--no-sandbox"); //샌드박스 모드 비활성화
        chromeOptions.addArguments("--disable-dev-shm-usage"); // /dev/shm 사용 비활성화
        chromeOptions.addArguments("--disable-gpu"); //GPU 가속 비활성화
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        this.driver = new ChromeDriver(chromeOptions);
    }

    public List<WebProductData> getPage() {
        String url = "https://www.nike.com/kr/launch?s=upcoming"; //나이키(Nike) SNKRS 런치 캘린더 KR
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        String html = driver.getPageSource();
        Document document = Jsoup.parse(html);

        Elements productCards = document.select(".d-md-h.ncss-col-sm-12.va-sm-t.pb0-sm.prl0-sm");
        ArrayList<WebProductData> results = new ArrayList<>();
        for (Element productCard : productCards) {

            // 제품명 추출
            String productName = productCard.select(".d-sm-tc .headline-5").text();

            // 출시날짜 추출
            String[] dates = productCard.select(".d-sm-tc .available-date-component").text().split("[\\.\\s]+");
            String month = dates[0];
            String day = dates[1];
            String ampm = dates[2];
            String time = dates[3].replace("출시", "");

            // 이미지 URL 추출
            String imageUrl = productCard.select(".card-link img.image-component").attr("src");

            // 결과 저장
            results.add(new WebProductData(productName, imageUrl, month, day, ampm, time));
        }
        return results;
    }
}
