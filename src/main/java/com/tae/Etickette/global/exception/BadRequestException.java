package com.tae.Etickette.global.exception;

public class BadRequestException extends BusinessException{
    public BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
