package com.focusup.global.apiPayload.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 일반 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "금지된 요청입니다."),
    
    // 토큰 에러
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 에러입니다."),

    // User 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),

    // Routine 에러
    ROUTINE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 루틴입니다."),
    // Item 에러
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
    ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 아이템입니다."),

    // Level 에러
    LEVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 레벨입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
