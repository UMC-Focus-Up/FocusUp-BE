package com.focusup.global.apiPayload.exception;

import com.focusup.global.apiPayload.code.ErrorCode;

public class AuthException extends CustomException {
    public AuthException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}