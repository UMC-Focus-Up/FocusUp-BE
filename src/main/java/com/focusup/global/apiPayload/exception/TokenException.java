package com.focusup.global.apiPayload.exception;

import com.focusup.global.apiPayload.code.ErrorCode;

public class TokenException extends CustomException {
    public TokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
