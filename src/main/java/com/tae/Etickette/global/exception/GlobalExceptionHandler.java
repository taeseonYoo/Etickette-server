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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error("ResourceNotFoundException : {}", e.getMessage());
        final ErrorResponse response = new ErrorResponse(e.getErrorCode().getCode(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    /**
     * 커스텀 예외 처리
     */
//    @ExceptionHandler(BusinessException.class)
//    public ResponseEntity<?> customExHandle(BusinessException e) {
//        log.error("BusinessException : {}", e.getMessage());
//        final ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getMessage());
//        return ResponseResult.fail(e);
//    }
//
//    /**
//     * RuntimeException
//     */
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<?> runtimeExHandle(RuntimeException e) {
//        log.error("RuntimeException : {}", e.getMessage());
//        return ResponseResult.fail(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
//    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handle(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : {}", e.getMessage());
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(403)
                .body("접근 권한이 없습니다.");
    }
}
