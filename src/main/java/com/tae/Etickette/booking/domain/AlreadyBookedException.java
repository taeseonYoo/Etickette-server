package com.tae.Etickette.booking.domain;

public class AlreadyBookedException extends RuntimeException {
    public AlreadyBookedException() {
        super();
    }

    public AlreadyBookedException(String message) {
        super(message);
    }

    public AlreadyBookedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyBookedException(Throwable cause) {
        super(cause);
    }

    protected AlreadyBookedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
