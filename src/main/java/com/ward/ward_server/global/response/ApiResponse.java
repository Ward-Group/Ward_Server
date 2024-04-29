package com.ward.ward_server.global.response;

import com.ward.ward_server.global.exception.ExceptionCode;
import com.ward.ward_server.global.response.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ApiResponse<T> {

    private final String message;
    private final int code;
    private final T data;

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<> (
                "성공",
                200,
                null
        );
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<> (
                "성공",
                200,
                data
        );
    }

    public static <T> ApiResponse<T> ok(ApiResponseMessage message) {
        return new ApiResponse<> (
                message.getMessage(),
                200,
                null
        );
    }

    public static <T> ApiResponse<T> ok(ApiResponseMessage message, T data) {
        return new ApiResponse<> (
                message.getMessage(),
                200,
                data
        );
    }
    public static <T> ApiResponse<T> create(){
        return new ApiResponse<> (
                "생성",
                200,
                null
        );
    }

    public static <T> ApiResponse<T> create(T data){
        return new ApiResponse<> (
                "생성",
                200,
                data
        );
    }

    public static <T> ApiResponse<T> failure(ExceptionCode exceptionCode) {
        return new ApiResponse<>(
                exceptionCode.getMessage(),
                exceptionCode.getCode(),
                null
        );
    }

    public static <T> ApiResponse<T> failure(ExceptionCode exceptionCode, T data) {
        return new ApiResponse<>(
                exceptionCode.getMessage(),
                exceptionCode.getCode(),
                data
        );
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(
                errorCode.getMessage(),
                errorCode.getCode(),
                null
        );
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, T data) {
        return new ApiResponse<>(
                errorCode.getMessage(),
                errorCode.getCode(),
                data
        );
    }
}
