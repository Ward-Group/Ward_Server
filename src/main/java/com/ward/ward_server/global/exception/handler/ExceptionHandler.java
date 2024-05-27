package com.ward.ward_server.global.exception.handler;

import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
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
        return ApiResponse.failure(ex.getExceptionCode());
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse handleException(IllegalArgumentException ex){
        return ApiResponse.error(DATA_TYPE_CONVERT_FAIL);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ApiResponse.failure(ExceptionCode.INVALID_INPUT);
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    public ApiResponse handleBadCredentialsException(BadCredentialsException ex) {
        return ApiResponse.failure(determineExceptionCode(ex.getMessage()));
    }

    private ExceptionCode determineExceptionCode(String message) {
        if (message.equals(ExceptionCode.NON_EXISTENT_EMAIL.getMessage())) {
            return ExceptionCode.NON_EXISTENT_EMAIL;
        } else if (message.equals(ExceptionCode.INVALID_USERNAME_OR_PASSWORD.getMessage())) {
            return ExceptionCode.INVALID_USERNAME_OR_PASSWORD;
        } else if (message.equals(ExceptionCode.INVALID_REFRESH_TOKEN.getMessage())) {
            return ExceptionCode.INVALID_REFRESH_TOKEN;
        } else if (message.equals(ExceptionCode.INVALID_EMAIL_FORMAT.getMessage())) {
            return ExceptionCode.INVALID_EMAIL_FORMAT;
        } else if (message.equals(ExceptionCode.INVALID_PASSWORD_FORMAT.getMessage())) {
            return ExceptionCode.INVALID_PASSWORD_FORMAT;
        } else if (message.equals(ExceptionCode.EMAIL_ALREADY_EXISTS.getMessage())) {
            return ExceptionCode.EMAIL_ALREADY_EXISTS;
        } else if (message.equals(ExceptionCode.DUPLICATE_NICKNAME.getMessage())) {
            return ExceptionCode.DUPLICATE_NICKNAME;
        }
        // 기본 예외 코드
        return ExceptionCode.INVALID_USERNAME_OR_PASSWORD;
    }
}
