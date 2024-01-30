package com.ward.ward_server.api.webcrawling;

import com.ward.ward_server.api.webcrawling.dto.WebProductData;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ward.ward_server.global.response.ApiResponseMessage.WEBCRAWLING_KASINA_SUCCESS;
import static com.ward.ward_server.global.response.ApiResponseMessage.WEBCRAWLING_NIKE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/wc")
public class WebCrawlingController {
    private final WebCrawlingService webCrawlingService;

    @GetMapping("/nike")
    public ApiResponse getNikeDate() {
        webCrawlingService.getNikeData();
        return ApiResponse.ok(WEBCRAWLING_NIKE_SUCCESS);
    }

    @GetMapping("/kasina")
    public ApiResponse getKasinaData() {
        webCrawlingService.getKasinaData();
        return ApiResponse.ok(WEBCRAWLING_KASINA_SUCCESS);
    }

    @GetMapping("/test")
    public ApiResponse<List<WebProductData>> testData() {
        List<WebProductData> datas = webCrawlingService.test();
        return ApiResponse.ok(WEBCRAWLING_NIKE_SUCCESS, datas);
    }
}
