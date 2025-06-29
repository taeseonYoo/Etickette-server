package com.tae.Etickette.member.application;

public class AlreadyExisingEmailException extends RuntimeException {
    public AlreadyExisingEmailException() {
        super();
    }

    public AlreadyExisingEmailException(String message) {
        super(message);
    }

    public AlreadyExisingEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExisingEmailException(Throwable cause) {
        super(cause);
    }

    protected AlreadyExisingEmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
