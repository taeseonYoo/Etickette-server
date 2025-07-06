package com.tae.Etickette.concert.command.domain;

public class ConcertNotReadyException extends RuntimeException {
    public ConcertNotReadyException() {
        super();
    }

    public ConcertNotReadyException(String message) {
        super(message);
    }

    public ConcertNotReadyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcertNotReadyException(Throwable cause) {
        super(cause);
    }

    protected ConcertNotReadyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
