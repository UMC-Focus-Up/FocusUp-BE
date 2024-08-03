package com.focusup.global.apiPayload;

import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.CustomException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().stream()
                .forEach(fieldError -> {
                    String fieldName = fieldError.getField();
                    String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                    errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Response.error("Request is incomplete.", errors));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        String message = e.getMessage();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(Response.error(message));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException e) {
        HttpStatus status = HttpStatus.valueOf(e.status());
        String message = String.format("FeignException: status %s, message: %s", status, e.getMessage());

        return ResponseEntity.status(status)
                .body(Response.error(message));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception e) {
        String message = e.getMessage();

        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(Response.error(message));
    }
}
