package com.ward.ward_server.api.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/v1/wc")
public class WebCrawlingController {
    //TODO 선착순 크롤링 시 발매일,마감일,당첨자 발표일 동일하게 입력 필수
//    private final WebCrawlingService webCrawlingService;
//
//    @GetMapping("/nike")
//    public ApiResponse getNikeDate() {
//        webCrawlingService.getNikeData();
//        return ApiResponse.ok(WEBCRAWLING_NIKE_SUCCESS);
//    }
//
//    @GetMapping("/kasina")
//    public ApiResponse getKasinaData() {
//        webCrawlingService.getKasinaData();
//        return ApiResponse.ok(WEBCRAWLING_KASINA_SUCCESS);
//    }
//
//    @GetMapping("/test")
//    public ApiResponse<List<WebProductData>> testData() {
//        List<WebProductData> datas = webCrawlingService.test();
//        return ApiResponse.ok(WEBCRAWLING_NIKE_SUCCESS, datas);
//    }
}
