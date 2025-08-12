package com.tae.Etickette.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
