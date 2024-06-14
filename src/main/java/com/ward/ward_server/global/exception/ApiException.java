package com.ward.ward_server.global.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ApiException extends RuntimeException {
    private final ExceptionCode exceptionCode;
    private List<String> messages;

    public ApiException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public ApiException(ExceptionCode exceptionCode, List<String> messages) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.messages = messages;
    }

    public ApiException(ExceptionCode exceptionCode, String message) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.messages = List.of(message);
    }
}
