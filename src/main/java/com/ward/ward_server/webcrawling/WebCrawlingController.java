package com.ward.ward_server.webcrawling;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/wc")
public class WebCrawlingController {
    private final WebCrawlingService webCrawlingService;

    @GetMapping("/nike")
    public ResponseEntity getNikeDate(){
        webCrawlingService.getNikeData();
        return ResponseEntity.ok("nike data success");
    }

    @GetMapping("/kasina")
    public ResponseEntity getKasinaData(){
        webCrawlingService.getKasinaData();
        return ResponseEntity.ok("kasina data success");
    }
}
