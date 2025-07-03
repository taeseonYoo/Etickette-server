package com.tae.Etickette.session.domain;

public class AlreadySessionOpenedException extends RuntimeException {
    public AlreadySessionOpenedException() {
    }

    public AlreadySessionOpenedException(String message) {
        super(message);
    }

    public AlreadySessionOpenedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadySessionOpenedException(Throwable cause) {
        super(cause);
    }

    public AlreadySessionOpenedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
