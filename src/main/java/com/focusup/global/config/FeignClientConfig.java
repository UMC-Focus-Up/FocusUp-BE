package com.focusup.global.config;

import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.CustomException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    public static class CustomErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() == 401) {
                return new CustomException(ErrorCode.UNAUTHORIZED);
            } else if (response.status() == 404) {
                return new CustomException(ErrorCode.NOT_FOUND);
            }

            throw FeignException.errorStatus(methodKey, response);
        }
    }
}