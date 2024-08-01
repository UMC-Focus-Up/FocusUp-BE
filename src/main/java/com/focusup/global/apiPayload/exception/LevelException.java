package com.focusup.global.apiPayload.exception;

import com.focusup.global.apiPayload.code.ErrorCode;

public class LevelException extends CustomException{
    public LevelException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public LevelException(ErrorCode errorCode) {
        super(errorCode);
    }
}
