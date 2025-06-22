package com.tae.Etickette.session.application;

public class AlreadyExistingDate extends RuntimeException {
    public AlreadyExistingDate() {
        super();
    }

    public AlreadyExistingDate(String message) {
        super(message);
    }

    public AlreadyExistingDate(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyExistingDate(Throwable cause) {
        super(cause);
    }

    protected AlreadyExistingDate(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
