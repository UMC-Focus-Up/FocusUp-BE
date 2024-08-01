package com.focusup.global.apiPayload.exception;

import com.focusup.global.apiPayload.code.ErrorCode;

public class RoutineException extends CustomException{
    public RoutineException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public RoutineException(ErrorCode errorCode) {
        super(errorCode);
    }
}
