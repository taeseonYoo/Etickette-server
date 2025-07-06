package com.tae.Etickette.concert.command.domain;

public class ConcertNotActiveException extends RuntimeException {
    public ConcertNotActiveException() {
        super();
    }

    public ConcertNotActiveException(String message) {
        super(message);
    }

    public ConcertNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcertNotActiveException(Throwable cause) {
        super(cause);
    }

    protected ConcertNotActiveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
