package com.focusup.global.apiPayload.exception;

import com.focusup.global.apiPayload.code.ErrorCode;

public class MemberException extends CustomException {
    public MemberException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}