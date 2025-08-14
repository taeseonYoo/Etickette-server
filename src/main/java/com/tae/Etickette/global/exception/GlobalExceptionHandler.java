package com.tae.Etickette.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<String> handle(MethodArgumentNotValidException e) {
//        log.error("MethodArgumentNotValidException : {}", e.getMessage());
//        String errorMessage = e.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(error -> error.getDefaultMessage())
//                .collect(Collectors.joining(", "));
//        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
//    }
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
//        return ResponseEntity
//                .status(403)
//                .body("접근 권한이 없습니다.");
//    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e) {
        log.error("ForbiddenException : {}", e.getMessage());
        final ErrorResponse response = new ErrorResponse(e.getErrorCode().getCode(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        log.error("BadRequestException : {}", e.getMessage());
        final ErrorResponse response = new ErrorResponse(e.getErrorCode().getCode(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error("ResourceNotFoundException : {}", e.getMessage());
        final ErrorResponse response = new ErrorResponse(e.getErrorCode().getCode(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("Undefined Business Exception : {}", e.getMessage());
        final ErrorResponse response = new ErrorResponse(e.getErrorCode().getCode(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handle not business exception", e);
        ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
