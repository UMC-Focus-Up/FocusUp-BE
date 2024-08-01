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
    ILLEGAL_REGISTRATION_ID(HttpStatus.UNAUTHORIZED, "유효하지 않은 등록 아이디입니다."),
    UNSUPPORTED_SOCIAL_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE,"지원하지 않는 소셜 로그인 입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
