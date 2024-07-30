//package com.ward.ward_server.controller;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.Matchers.containsStringIgnoringCase;
//import  static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class HelloControllerTest {
//    @Autowired
//    private MockMvc api;
//
//    // 어떤 요청에도 HTTP 상태코드 200, HELLO 가 포함되어있는지 테스트
//    @Test
//    void anyoneCanViewPublicEndpoint() throws Exception {
//        api.perform(get("/"))
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsStringIgnoringCase("Hello")));
//    }
//
//    // 로그인 안 한 사용자가 접근했을 때 HTTP 상태코드 401인지 테스트
//    @Test
//    void notLoggedIn_shouldNotSeeSecuredEndpoint() throws Exception {
//        api.perform(get("/secured"))
//                .andExpect(status().isUnauthorized());
//    }
//
//    // 로그인한 사용자가 접근했을 때 HTTP응답의 상태코드가 200 OK 인지, 작성한 test 포함하는지 확인하는 테스트
////    @Test
////    @WithMockUser
////    void loggedIn_shouldSeeSecuredEndpoint() throws Exception {
////        api.perform(get("/secured"))
////                .andExpect(status().isOk())
////                .andExpect(content().string(containsStringIgnoringCase("User ID: 1")));
////    }
//
//    // 로그인 안하고 /admin 접근할 때 401
//    @Test
//    void notLoggedIn_shouldNotSeeAdminEndpoint() throws Exception {
//        api.perform(get("/admin"))
//                .andExpect(status().isUnauthorized());
//    }
//
//    //로그인 하고 ROLE_USER 일 때, /admin 접근 제한
//    @Test
//    @WithMockUser
//    void simpleUser_shouldNotSeeAdminEndpoint() throws Exception {
//        api.perform(get("/admin"))
//                .andExpect(status().isForbidden());
//    }
//
//    // 로그인하고 ROLE_ADMIN 일 때, /admin 접근 성공+ 작성한 문구 포함 확인
//    @Test
//    @WithAdminUser
//    void admin_shouldSeeAdminEndpoint() throws Exception {
//        api.perform(get("/admin"))
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsStringIgnoringCase("User ID: 1")));
//    }
//}