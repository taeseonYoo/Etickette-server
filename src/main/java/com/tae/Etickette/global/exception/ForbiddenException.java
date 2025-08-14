package com.tae.Etickette.global.exception;

public class ForbiddenException extends BusinessException {
    public ForbiddenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
