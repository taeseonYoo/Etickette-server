package com.tae.Etickette.session.domain;

public class AlreadyStartedException extends RuntimeException {
    public AlreadyStartedException() {
        super();
    }

    public AlreadyStartedException(String message) {
        super(message);
    }

    public AlreadyStartedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyStartedException(Throwable cause) {
        super(cause);
    }

    protected AlreadyStartedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
