package com.ward.ward_server.global.exception.handler;

import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class CustomResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return (
                response.getStatusCode().is4xxClientError() ||
                        response.getStatusCode().is5xxServerError()
        );
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatusCode statusCode = response.getStatusCode();
        if (statusCode.is4xxClientError()) {
            throw new ApiException(ExceptionCode.SOCIAL_DISCONNECT_FAILED, "계정 연동 해제 실패");
        } else if (statusCode.is5xxServerError()) {
            throw new ApiException(ExceptionCode.SERVER_ERROR, "서버 오류가 발생했습니다.");
        }
    }
}
