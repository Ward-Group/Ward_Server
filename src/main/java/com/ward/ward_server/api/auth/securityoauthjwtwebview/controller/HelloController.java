package com.ward.ward_server.api.auth.securityoauthjwtwebview.controller;

import com.ward.ward_server.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/")
    @ResponseBody
    public String mainAPI() {
        return "main route";
    }

    @GetMapping("/my")
    @ResponseBody
    public String myAPI() {
        return "my route";
    }

    @GetMapping("/hello")
    @ResponseBody
    public ApiResponse greeting(){
        String helloWorld = "Hello, World";
        return ApiResponse.ok(helloWorld);
    }

}
