package com.ward.ward_server.global.exception.handler;

import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.ward.ward_server.global.response.error.ErrorCode.DATA_TYPE_CONVERT_FAIL;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(ApiException.class)
    public ApiResponse handleException(ApiException ex){
        return ApiResponse.failure(ex.getExceptionCode(), ex.getMessages());
    }
}
