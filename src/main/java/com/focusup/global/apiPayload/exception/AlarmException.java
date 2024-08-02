package com.focusup.global.apiPayload.exception;

import com.focusup.global.apiPayload.code.ErrorCode;

public class AlarmException extends CustomException {
    public AlarmException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AlarmException(ErrorCode errorCode) {
        super(errorCode);
    }
}
