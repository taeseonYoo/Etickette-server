package com.tae.Etickette.concert.command.domain;

public class ConcertNotOpenException extends RuntimeException {
    public ConcertNotOpenException() {
        super();
    }

    public ConcertNotOpenException(String message) {
        super(message);
    }

    public ConcertNotOpenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcertNotOpenException(Throwable cause) {
        super(cause);
    }

    protected ConcertNotOpenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
