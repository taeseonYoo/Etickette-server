package com.tae.Etickette.global.exception;

public class ServerProcessException extends BusinessException {

    public ServerProcessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
