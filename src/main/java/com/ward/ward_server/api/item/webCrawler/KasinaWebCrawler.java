package com.ward.ward_server.api.item.webCrawler;

import com.ward.ward_server.api.item.dto.WebProductData;
import com.ward.ward_server.api.item.entity.enumtype.Brand;
import com.ward.ward_server.api.item.entity.enumtype.State;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class KasinaWebCrawler {
    //FIXME 나중에 사이트별로 웹 크롤러 만들때 다 쓰이니까 전역변수로 만들어줘야 한다.
    private String CHROME_DRIVER_PATH = "D:\\chromedriver/chromedriver-win64/chromedriver.exe";
    private WebDriver driver;

    @Autowired
    public KasinaWebCrawler() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless=new"); //헤드리스 모드로 실행, 실제 창이 표시되지 않는다.
        chromeOptions.addArguments("--lang=ko"); //브라우저 언어를 한국어로 설정
        chromeOptions.addArguments("--no-sandbox"); //샌드박스 모드 비활성화
        chromeOptions.addArguments("--disable-dev-shm-usage"); // /dev/shm 사용 비활성화
        chromeOptions.addArguments("--disable-gpu"); //GPU 가속 비활성화
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        this.driver = new ChromeDriver(chromeOptions);
    }

    public ArrayList<WebProductData> getData() {
        String url = "https://www.kasina.co.kr/launches"; //카시나(Kasina) 런처
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        java.util.List<WebElement> items = driver.findElements(By.className("c-card__link"));

        ArrayList<String> subUrl = new ArrayList<>();
        for (WebElement item : items) {
            String href = item.getAttribute("href");
            System.out.println(href);
            subUrl.add(href);
        }

        ArrayList<WebProductData> results = new ArrayList<>();
        for (String next : subUrl) {
            driver.get(next);
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            String html = driver.getPageSource();
            Document document = Jsoup.parse(html);

            // 1.제품명 추출
            String productName = document.select(".dtl-title .sub-txt").first().text();
            productName = productName.replace("[앱 전용]", "").trim();
            log.debug("상품명 {}", productName);

            // 2.이미지 URL 추출
            String imageUrl = document.select(".c-lazyload c-lazyload--ratio_normal c-lazyload--gray").first().attr("src");
            log.debug("이미지 {}", imageUrl);

            // 3.사이트 URL 추출
            String siteUrl = null;

            // 4.날짜 추출
            String stringDate = document.select(".dtl-raffle__info dd").text();
            log.debug("총 날짜 {}", stringDate);
            List<LocalDateTime> dates = getDates(stringDate);
            if (dates == null) continue;
            LocalDateTime releaseDate = dates.get(0);
            LocalDateTime dueDate = dates.get(1);
            LocalDateTime presentationDate = dates.get(2);
            log.debug("응모 {}, 기한 {}, 발표 {}", releaseDate, dueDate, presentationDate);

            // 5.상태
            LocalDateTime now=LocalDateTime.now();
            State state=State.IMPOSSIBLE;
            if(now.isAfter(releaseDate)) state=State.POSSIBLE;

            // 6.브랜드 KASINA

            // 결과 저장
            results.add(new WebProductData(productName, imageUrl, siteUrl,
                    releaseDate, dueDate, presentationDate,
                    state, Brand.KASINA));
        }
        return results;
    }

    /*
     * 응모시작시간, 응모마감시간, 발표시간 셋 중 하나라도 빠져있으면 해당 데이터는 입력하지 않는다.
     * */
    private List<LocalDateTime> getDates(String strDate) {
        String regex = "(\\d{4}\\.\\d{2}\\.\\d{2} \\(\\S+\\) \\d{2}:\\d{2}) ~ (\\d{4}\\.\\d{2}\\.\\d{2} \\(\\S+\\) \\d{2}:\\d{2})";
        // \d 숫자
        // {4} 4번 반복
        // \S 공백이 아닌 모든 문자
        // + 하나 이상
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(strDate);
        StringBuffer resultString = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(resultString, "**$1 ~ $2**");
        }
        matcher.appendTail(resultString);
        String[] splitStringDate = resultString.toString().split("\\*\\*");
        List<String> strDateList = new ArrayList<>();
        for (String str : splitStringDate) {
            if (str != null && !str.isEmpty()) {
                strDateList.add(str);
            }
        }
        List<LocalDateTime> localDateTimeList = new ArrayList<>();
        if (strDateList.size() == 3) {
            String[] dates = Arrays.stream(strDateList.get(0).split("~"))
                    .map(e -> e.trim())
                    .toArray(String[]::new); //응모시작시간 ~ 응모마감시간 분리
            log.debug("확인:{}", Arrays.toString(dates));
            String[] replacedDates = Arrays.stream(dates)
                    .map(e -> e.replaceAll(" \\(\\S\\) ", "."))
                    .toArray(String[]::new);
            log.debug("추가후:{}", Arrays.toString(replacedDates));
            String[] splittedReleaseDate = replacedDates[0].split("\\.");
            log.debug("분리:{}", Arrays.toString(splittedReleaseDate));
            String[] splittedReleaseDateTime = splittedReleaseDate[3].split(":");
            LocalDateTime releaseDate = LocalDateTime.of(
                    Integer.parseInt(splittedReleaseDate[0]),
                    Integer.parseInt(splittedReleaseDate[1]),
                    Integer.parseInt(splittedReleaseDate[2]),
                    Integer.parseInt(splittedReleaseDateTime[0]),
                    Integer.parseInt(splittedReleaseDateTime[1]));
            localDateTimeList.add(releaseDate);

            String[] splittedDueDate = replacedDates[1].split("\\.");
            String[] splittedDueDateTime = splittedDueDate[3].split(":");
            LocalDateTime dueDate = LocalDateTime.of(
                    Integer.parseInt(splittedDueDate[0]),
                    Integer.parseInt(splittedDueDate[1]),
                    Integer.parseInt(splittedDueDate[2]),
                    Integer.parseInt(splittedDueDateTime[0]),
                    Integer.parseInt(splittedDueDateTime[1]));
            localDateTimeList.add(dueDate);

            String presentationStrDate = strDateList.get(1).trim();
            presentationStrDate = presentationStrDate.replaceAll(" \\(\\S\\) ", ".");
            String[] splittedPresentationDate = presentationStrDate.split("\\.");
            log.debug("분리:{}", Arrays.toString(splittedPresentationDate));
            String[] splittedPresentationDateTime = splittedPresentationDate[3].split(":");
            LocalDateTime presentationDate = LocalDateTime.of(
                    Integer.parseInt(splittedPresentationDate[0]),
                    Integer.parseInt(splittedPresentationDate[1]),
                    Integer.parseInt(splittedPresentationDate[2]),
                    Integer.parseInt(splittedPresentationDateTime[0]),
                    Integer.parseInt(splittedPresentationDateTime[1]));
            localDateTimeList.add(presentationDate);

        }

        if (localDateTimeList.size() == 3) return localDateTimeList;
        else return null;
    }
}
