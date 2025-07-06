package com.tae.Etickette.concert.command.domain;

public class ConcertNotCloseException extends RuntimeException {
    public ConcertNotCloseException() {
    }

    public ConcertNotCloseException(String message) {
        super(message);
    }

    public ConcertNotCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcertNotCloseException(Throwable cause) {
        super(cause);
    }

    public ConcertNotCloseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
