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
    NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다."),
    
    // 토큰 에러
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    TOKEN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 에러입니다."),

    // User 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    ILLEGAL_REGISTRATION_ID(HttpStatus.UNAUTHORIZED, "유효하지 않은 등록 아이디입니다."),
    UNSUPPORTED_SOCIAL_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE,"지원하지 않는 소셜 로그인 입니다."),
    INSUFFICIENT_LIFE(HttpStatus.BAD_REQUEST, "생명이 부족합니다."),

    // Routine 에러
    ROUTINE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 루틴입니다."),
    USER_ROUTINE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저 루틴입니다."),
    DELAY_COUNT_OVER(HttpStatus.BAD_REQUEST, "미루기 기능은 최대 6번까지만 가능합니다."),

    // Item 에러
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
    ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 아이템입니다."),

    // Level 에러
    LEVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 레벨입니다."),
    LEVEL_TOO_HIGH(HttpStatus.BAD_REQUEST, "최고 레벨은 7레벨이며 그 이상으로는 올라갈 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
