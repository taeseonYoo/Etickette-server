package com.tae.Etickette.global.exception;

public class ConflictException extends BusinessException{
    public ConflictException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
