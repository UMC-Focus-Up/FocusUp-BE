package com.focusup.global.apiPayload.exception;

import com.focusup.global.apiPayload.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}