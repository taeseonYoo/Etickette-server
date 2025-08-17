package com.tae.Etickette.global.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends BusinessException{
    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
