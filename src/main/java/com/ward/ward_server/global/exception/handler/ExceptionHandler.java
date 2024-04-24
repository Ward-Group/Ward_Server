package com.ward.ward_server.global.exception.handler;

import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ward.ward_server.global.response.error.ErrorCode.DATA_TYPE_CONVERT_FAIL;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
    public ApiResponse<String> handleException(ApiException ex){
        return ApiResponse.failure(ex.getExceptionCode());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<String> handleException(IllegalArgumentException ex){
        return ApiResponse.error(DATA_TYPE_CONVERT_FAIL);
    }
}
