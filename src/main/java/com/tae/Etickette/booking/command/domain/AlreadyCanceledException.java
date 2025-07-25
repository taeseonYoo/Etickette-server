package com.tae.Etickette.booking.command.domain;

public class AlreadyCanceledException extends RuntimeException {
    public AlreadyCanceledException() {
        super();
    }

    public AlreadyCanceledException(String message) {
        super(message);
    }

    public AlreadyCanceledException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyCanceledException(Throwable cause) {
        super(cause);
    }

    protected AlreadyCanceledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
