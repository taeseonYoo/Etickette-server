package com.tae.Etickette.concert;

public class ConcertNotFoundException extends RuntimeException {
    public ConcertNotFoundException() {
        super();
    }

    public ConcertNotFoundException(String message) {
        super(message);
    }

    public ConcertNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConcertNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ConcertNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
